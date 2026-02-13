package br.com.pioriam.leadscope.modelos;

import br.com.pioriam.leadscope.modelos.Member;
import lombok.Data;

import java.util.List;
@Data
public class CompanyResponse {

    private Long id;
    private String name;
    private List<Member> members;

    // getters e setters
}