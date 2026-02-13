package br.com.pioriam.leadscope.servicos;

import br.com.pioriam.leadscope.modelos.*;
import br.com.pioriam.leadscope.repositorio.ProspectarRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
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

    public void buscarDados(List<Map<String, String>> mapa) {

        List<String> ListaCnpjBaseConciliadora = mapa.stream()
                .flatMap(m -> m.values().stream())
                .toList();
        List<Person> listaPrimeiraPesquisa = new ArrayList<>();
        List<Person> listaSocioTotal = new ArrayList<>();
        List<Company> listaEmpresasSocios = new ArrayList<>();

        List<Map<String,String> > ListaObj = new  ArrayList<>();;

        String primeiraEmpresa = inicializadorPesquisa(ListaCnpjBaseConciliadora, listaPrimeiraPesquisa);

        //pega o primeiro socio da lista da empresa

        for (Person person : listaPrimeiraPesquisa) {
             var id = person.getId();
            aguardarLimite();

            PersonResponse personResponse = executarComRetry(() ->
                    restClient.get()
                            .uri("/person/{id}", id)
                            .retrieve()
                            .body(PersonResponse.class)
            );

            for (Membership membership : personResponse.getMembership()) {

                Company company = membership.getCompany();

                if (!primeiraEmpresa.contains(company.getName())) {
                    var comp = new Company();
                    comp.setId(membership.getCompany().getId());
                    comp.setName(membership.getCompany().getName());
                    comp.setSocioNome(person);
                    listaEmpresasSocios.add(comp);
                }

                aguardarLimite();

                CompanyResponse companyResponse = executarComRetry(() ->
                        restClient.get()
                                .uri("/company/{id}", company.getId())
                                .retrieve()
                                .body(CompanyResponse.class)
                );

                List<Member> members = companyResponse.getMembers();
                company.setMembers(members);

                System.out.println(company);

                for (Member member : members) {
                    Person pp = member.getPerson();
                    listaSocioTotal.add(pp);

                }

            }
        }













//        System.out.println("----------------------------");
//        System.out.println("Sócios da primeira empresa:");
//        System.out.println(listaPrimeiraPesquisa);

        System.out.print("\uD83D\uDC49\uD83D\uDC49\uD83D\uDC49\uD83D\uDC49");
        System.out.println("Empresas relacionadas:");
        System.out.println(listaEmpresasSocios);

        System.out.print("\uD83D\uDC49\uD83D\uDC49\uD83D\uDC49\uD83D\uDC49 ");
        System.out.println("Sócios das empresas relacionadas:");
        System.out.println(listaSocioTotal);
    }

    private String inicializadorPesquisa( List<String> ListaCnpjBase , List<Person> listaPrimeiraPesquisa) {

        for (String cnpjbase : ListaCnpjBase){

            OfficeResponse response = executarComRetry(() ->
                    restClient.get()
                            .uri("/office/{cnpjbase}", cnpjbase)
                            .retrieve()
                            .body(OfficeResponse.class)
            );

            for (Member member : response.getCompany().getMembers()) {
                listaPrimeiraPesquisa.add(member.getPerson());
            }
            return response.getCompany().getName();
        }
        return "";
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
