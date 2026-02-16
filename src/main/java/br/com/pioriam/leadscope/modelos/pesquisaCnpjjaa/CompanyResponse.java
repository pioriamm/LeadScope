package br.com.pioriam.leadscope.modelos.pesquisaCnpjjaa;

import lombok.Data;

import java.util.List;
@Data
public class CompanyResponse {

    private Long id;
    private String name;
    private List<Member> members;

    // getters e setters
}