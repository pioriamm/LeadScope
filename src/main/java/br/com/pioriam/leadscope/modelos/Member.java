package br.com.pioriam.leadscope.modelos;

import lombok.Data;

@Data
public class Member {

    private String since;
    private Person person;
    private Role role;
}
