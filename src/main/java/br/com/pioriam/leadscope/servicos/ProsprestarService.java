package br.com.pioriam.leadscope.servicos;

import br.com.pioriam.leadscope.modelos.*;
import br.com.pioriam.leadscope.repositorio.ProspectarRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
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
                .defaultHeader("Authorization",  token)
                .defaultHeader("Accept", "application/json")
                .build();
    }

    public void buscarDados(Map<String, String> request) {

        String cnpj = request.get("cnpj");

        List<Person> listaPrimeiraPesquisa = new ArrayList<>();
        List<Person> listaSocioTotal = new ArrayList<>();
        List<Company> listaEmpresasSocios = new ArrayList<>();

        String primeiraEmpresa = inicializadorPesquisa(cnpj, listaPrimeiraPesquisa);

        for (Person person : listaPrimeiraPesquisa) {

            aguardarLimite();

            PersonResponse personResponse = executarComRetry(() ->
                    restClient.get()
                            .uri("/person/{id}", person.getId())
                            .retrieve()
                            .body(PersonResponse.class)
            );

            for (Membership membership : personResponse.getMembership()) {

                Company company = membership.getCompany();

                // comparação correta de String
                if (!primeiraEmpresa.contains(company.getName())) {
                    listaEmpresasSocios.add(company);
                }

                aguardarLimite();

                Member member = executarComRetry(() ->
                        restClient.get()
                                .uri("/company/{id}", company.getId())
                                .retrieve()
                                .body(Member.class)
                );

                if (member != null) {
                    listaSocioTotal.add(member.getPerson());
                }
            }
        }

        System.out.println("Sócios da primeira empresa:");
        System.out.println(listaPrimeiraPesquisa);

        System.out.println("Empresas relacionadas:");
        System.out.println(listaEmpresasSocios);

        System.out.println("Sócios das empresas relacionadas:");
        System.out.println(listaSocioTotal);
    }

    private String inicializadorPesquisa(String cnpj, List<Person> listaPrimeiraPesquisa) {

        OfficeResponse response = executarComRetry(() ->
                restClient.get()
                        .uri("/office/{cnpj}", cnpj)
                        .retrieve()
                        .body(OfficeResponse.class)
        );

        for (Member member : response.getCompany().getMembers()) {
            listaPrimeiraPesquisa.add(member.getPerson());
        }

        return response.getCompany().getName();
    }

    // =====================================
    // 🔹 CONTROLE DE RATE LIMIT (6s)
    // =====================================
    private void aguardarLimite() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // =====================================
    // 🔹 RETRY AUTOMÁTICO PARA 429
    // =====================================
    private <T> T executarComRetry(SupplierWithException<T> supplier) {

        while (true) {
            try {
                return supplier.get();
            } catch (HttpClientErrorException.TooManyRequests e) {

                String body = e.getResponseBodyAsString();
                int ttl = extrairTTL(body);

                System.out.println("Rate limit atingido. Aguardando " + ttl + " segundos...");

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
