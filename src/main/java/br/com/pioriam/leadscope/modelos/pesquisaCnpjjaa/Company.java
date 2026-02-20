package br.com.pioriam.leadscope.modelos.pesquisaCnpjjaa;

import lombok.Data;

import java.util.List;

@Data
public class Company {

    private List<Member> members;
    private Long id;
    private String name;
    private List<Office> ListOffice;
}