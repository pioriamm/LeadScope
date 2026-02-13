package br.com.pioriam.leadscope.servicos;

import br.com.pioriam.leadscope.modelos.*;
import br.com.pioriam.leadscope.repositorio.ProspectarRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProsprestarService {

    private final ProspectarRepositorio prospectarRepositorio;
    private final RestClient restClient;

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
                .toList();

        List<Map<String, Object>> listaFinal = new ArrayList<>();

        for (String cnpjBase : listaCnpjBase) {

            aguardarLimite();

            Map<String, Object> dadosFinal = new LinkedHashMap<>();

            OfficeResponse response = executarComRetry(() ->
                    restClient.get()
                            .uri("/office/{cnpj}", cnpjBase)
                            .retrieve()
                            .body(OfficeResponse.class)
            );

            dadosFinal.put("compania_id", response.getCompany().getId());
            dadosFinal.put("cnpj_raiz_id", response.getTaxId());
            dadosFinal.put("empresa_raiz", response.getCompany().getName());


            dadosFinal.put("alias", response.getAlias());
            dadosFinal.put("email", response.getEmails());
            dadosFinal.put("telefone", response.getPhones());
            dadosFinal.put("status", response.getStatus());



            List<Map<String, Object>> listaMembros = new ArrayList<>();

            for (Member membro : response.getCompany().getMembers()) {

                aguardarLimite();

                Map<String, Object> membroMap = new LinkedHashMap<>();

                String idMembro = membro.getPerson().getId();

                membroMap.put("id_membro", idMembro);
                membroMap.put("nome_membro", membro.getPerson().getName());

                PersonResponse personResponse = executarComRetry(() ->
                        restClient.get()
                                .uri("/person/{id}", idMembro)
                                .retrieve()
                                .body(PersonResponse.class)
                );

                List<Map<String, Object>> listaEmpresasSocio = new ArrayList<>();

                for (Membership membership : personResponse.getMembership()) {

                    aguardarLimite();

                    Map<String, Object> empresaMap = new LinkedHashMap<>();

                    String idEmpresaSocio =
                            String.valueOf(membership.getCompany().getId());

                    empresaMap.put("id_empresa_socio", idEmpresaSocio);
                    empresaMap.put("nome_empresa_socio",
                            membership.getCompany().getName());

                    CompanyResponse companyResponse = executarComRetry(() ->
                            restClient.get()
                                    .uri("/company/{id}", idEmpresaSocio)
                                    .retrieve()
                                    .body(CompanyResponse.class)
                    );

                    empresaMap.put("membros_empresa_socio",
                            companyResponse.getMembers());

                    listaEmpresasSocio.add(empresaMap);
                }

                membroMap.put("empresas", listaEmpresasSocio);
                listaMembros.add(membroMap);
            }

            dadosFinal.put("membros", listaMembros);

            listaFinal.add(dadosFinal);
        }

        return listaFinal;
    }

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

                System.out.println("Rate limit atingido. Aguardando "
                        + ttl + " segundos...");

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
