package br.com.pioriam.leadscope.modelos;

import lombok.Data;

@Data
public class Membership {

    private String since;
    private Role role;
    private Company company;
}
