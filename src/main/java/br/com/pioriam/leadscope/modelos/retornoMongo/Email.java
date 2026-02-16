package br.com.pioriam.leadscope.modelos.retornoMongo;

import lombok.Data;

@Data
public class Email {

    private String ownership;
    private String address;
    private String domain;
    private String _class;

    // getters e setters
}
