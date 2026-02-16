package br.com.pioriam.leadscope.servicos;

import br.com.pioriam.leadscope.modelos.pesquisaCnpjjaa.*;
import br.com.pioriam.leadscope.repositorio.ProspectarRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.*;

@Service
public class ProsprestarService {

    private final ProspectarRepositorio prospectarRepositorio;
    private final RestClient restClient;

    // 🔥 Cache para evitar chamadas repetidas
    private final Map<String, PersonResponse> personCache = new HashMap<>();
    private final Map<String, CompanyResponse> companyCache = new HashMap<>();

    public ProsprestarService(ProspectarRepositorio prospectarRepositorio,
                              @Value("${cnpja.token}") String token) {

        this.prospectarRepositorio = prospectarRepositorio;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.cnpja.com")
                .defaultHeader("Authorization", token)
                .defaultHeader("Accept", "application/json")
                .build();
    }

    public List<Map<String, Object>> buscarDados(List<Map<String, String>> mapa) {

        List<String> listaCnpjBase = mapa.stream()
                .flatMap(m -> m.values().stream())
                .distinct()
                .toList();

        List<Map<String, Object>> listaFinal = new ArrayList<>(listaCnpjBase.size());

        for (String cnpjBase : listaCnpjBase) {

            OfficeResponse office = buscarOffice(cnpjBase);
            Map<String, Object> dadosEmpresa = montarEmpresa(office);

            listaFinal.add(dadosEmpresa);
        }

        Prospectar prospectar = new Prospectar();
        prospectar.setDados(listaFinal);
        prospectarRepositorio.save(prospectar);

        return listaFinal;
    }

    // ==========================
    // 🔥 MÉTODOS SEPARADOS
    // ==========================

    private OfficeResponse buscarOffice(String cnpj) {
        aguardarLimite();
        return executarComRetry(() ->
                restClient.get()
                        .uri("/office/{cnpj}", cnpj)
                        .retrieve()
                        .body(OfficeResponse.class)
        );
    }

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

    private Map<String, Object> montarMembro(Member membro) {

        aguardarLimite();

        String idMembro = membro.getPerson().getId();

        Map<String, Object> membroMap = new LinkedHashMap<>();
        membroMap.put("id_membro", idMembro);
        membroMap.put("nome_membro", membro.getPerson().getName());

        PersonResponse personResponse = personCache.computeIfAbsent(
                idMembro,
                this::buscarPerson
        );

        List<Map<String, Object>> empresas = personResponse.getMembership()
                .stream()
                .map(this::montarEmpresaSocio)
                .toList();

        membroMap.put("empresas", empresas);

        return membroMap;
    }

    private PersonResponse buscarPerson(String id) {
        return executarComRetry(() ->
                restClient.get()
                        .uri("/person/{id}", id)
                        .retrieve()
                        .body(PersonResponse.class)
        );
    }

    private Map<String, Object> montarEmpresaSocio(Membership membership) {

        aguardarLimite();

        String idEmpresa = String.valueOf(membership.getCompany().getId());

        Map<String, Object> empresaMap = new LinkedHashMap<>();
        empresaMap.put("id_empresa_socio", idEmpresa);
        empresaMap.put("nome_empresa_socio",
                membership.getCompany().getName());

        CompanyResponse companyResponse = companyCache.computeIfAbsent(
                idEmpresa,
                this::buscarCompany
        );

        List<Person> membrosEmpresa = companyResponse.getMembers()
                .stream()
                .map(Member::getPerson)
                .toList();

        empresaMap.put("membros_empresa_socio", membrosEmpresa);

        return empresaMap;
    }

    private CompanyResponse buscarCompany(String id) {
        return executarComRetry(() ->
                restClient.get()
                        .uri("/company/{id}", id)
                        .retrieve()
                        .body(CompanyResponse.class)
        );
    }

    // ==========================
    // RATE LIMIT
    // ==========================

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
