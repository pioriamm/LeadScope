package br.com.pioriam.leadscope.servicos;

import br.com.pioriam.leadscope.modelos.pesquisaCnpjjaa.*;
import br.com.pioriam.leadscope.repositorio.ProspectarRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProsprestarService {

    private final ProspectarRepositorio prospectarRepositorio;
    private final RestClient restClient;

    // ================= CACHE GLOBAL =================
    private final Map<String, PersonResponse> personCache = new ConcurrentHashMap<>();
    private final Map<String, CompanyResponse> companyCache = new ConcurrentHashMap<>();
    private final Map<String, OfficeResponse> officeCache = new ConcurrentHashMap<>();

    public ProsprestarService(
            ProspectarRepositorio prospectarRepositorio,
            @Value("${cnpja.token}") String token) {

        this.prospectarRepositorio = prospectarRepositorio;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.cnpja.com")
                .defaultHeader("Authorization", token)
                .defaultHeader("Accept", "application/json")
                .build();
    }

    // =====================================================
    // ENTRY POINT
    // =====================================================

    public List<Map<String, Object>> buscarDados(List<Map<String, String>> mapa) {

        List<String> listaCnpjBase = mapa.stream()
                .flatMap(m -> m.values().stream())
                .distinct()
                .toList();

        List<Map<String, Object>> listaFinal = new ArrayList<>();

        for (String cnpjBase : listaCnpjBase) {

            OfficeResponse office = buscarOfficeCached(cnpjBase);

            Map<String, Object> empresa = montarEmpresa(office);

            listaFinal.add(empresa);
        }

        Prospectar prospectar = new Prospectar();
        prospectar.setDados(listaFinal);
        prospectarRepositorio.save(prospectar);

        return listaFinal;
    }

    // =====================================================
    // BUILD EMPRESA RAIZ
    // =====================================================

    private Map<String, Object> montarEmpresa(OfficeResponse response) {

        Map<String, Object> dados = new LinkedHashMap<>();

        dados.put("compania_id", response.getCompany().getId());
        dados.put("cnpj_raiz_id", response.getTaxId());
        dados.put("empresa_raiz", response.getCompany().getName());
        dados.put("alias", response.getAlias());
        dados.put("email", response.getEmails());
        dados.put("telefone", response.getPhones());
        dados.put("status", response.getStatus());

        List<Map<String, Object>> membros = response.getCompany()
                .getMembers()
                .stream()
                .map(this::montarMembro)
                .toList();

        dados.put("membros", membros);

        return dados;
    }

    // =====================================================
    // MEMBRO
    // =====================================================

    private Map<String, Object> montarMembro(Member membro) {

        String idPessoa = membro.getPerson().getId();

        PersonResponse person = personCache.computeIfAbsent(
                idPessoa,
                this::buscarPerson
        );

        Map<String, Object> membroMap = new LinkedHashMap<>();
        membroMap.put("id_membro", idPessoa);
        membroMap.put("nome_membro", membro.getPerson().getName());

        List<Map<String, Object>> empresas = person.getMembership()
                .stream()
                .map(this::montarEmpresaSocio)
                .toList();

        membroMap.put("empresas", empresas);

        return membroMap;
    }

    // =====================================================
    // EMPRESA DO SÓCIO (ULTRA OTIMIZADO)
    // =====================================================

    private Map<String, Object> montarEmpresaSocio(Membership membership) {

        String companyId = String.valueOf(membership.getCompany().getId());

        CompanyResponse company = companyCache.computeIfAbsent(
                companyId,
                this::buscarCompany
        );

        Map<String, Object> empresaMap = new LinkedHashMap<>();
        empresaMap.put("id_empresa_socio", companyId);
        empresaMap.put("nome_empresa_socio", company.getName());

        // membros empresa
        List<Person> membrosEmpresa = company.getMembers()
                .stream()
                .map(Member::getPerson)
                .toList();

        empresaMap.put("membros_empresa_socio", membrosEmpresa);

        // ======================
        // OFFICE MATRIZ
        // ======================

        company.getOffices()
                .stream()
                .filter(o -> Boolean.TRUE.equals(o.getHead()))
                .findFirst()
                .ifPresent(officeInfo -> {

                    String taxId = officeInfo.getTaxId();

                    OfficeResponse office = officeCache.computeIfAbsent(
                            taxId,
                            this::buscarOffice
                    );

                    empresaMap.put("telefone", office.getPhones());
                    empresaMap.put("email", office.getEmails());
                    empresaMap.put("status", office.getStatus());
                });

        return empresaMap;
    }

    // =====================================================
    // API CALLS COM CACHE
    // =====================================================

    private OfficeResponse buscarOfficeCached(String taxId) {
        return officeCache.computeIfAbsent(
                taxId,
                this::buscarOffice
        );
    }

    private OfficeResponse buscarOffice(String taxId) {
        aguardarLimite();
        return executarComRetry(() ->
                restClient.get()
                        .uri("/office/{taxId}", taxId)
                        .retrieve()
                        .body(OfficeResponse.class)
        );
    }

    private CompanyResponse buscarCompany(String id) {
        aguardarLimite();
        return executarComRetry(() ->
                restClient.get()
                        .uri("/company/{id}", id)
                        .retrieve()
                        .body(CompanyResponse.class)
        );
    }

    private PersonResponse buscarPerson(String id) {
        aguardarLimite();
        return executarComRetry(() ->
                restClient.get()
                        .uri("/person/{id}", id)
                        .retrieve()
                        .body(PersonResponse.class)
        );
    }

    // =====================================================
    // RATE LIMIT HANDLER
    // =====================================================

    private void aguardarLimite() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private <T> T executarComRetry(SupplierWithException<T> supplier) {

        while (true) {
            try {
                return supplier.get();
            } catch (HttpClientErrorException.TooManyRequests e) {

                int ttl = extrairTTL(e.getResponseBodyAsString());

                try {
                    Thread.sleep(ttl * 1000L);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private int extrairTTL(String json) {
        try {
            String ttlPart = json.split("\"ttl\":")[1];
            return Integer.parseInt(ttlPart.split("[,}]")[0]);
        } catch (Exception e) {
            return 60;
        }
    }

    @FunctionalInterface
    interface SupplierWithException<T> {
        T get();
    }
}