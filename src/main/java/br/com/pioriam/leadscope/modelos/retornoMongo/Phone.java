package br.com.pioriam.leadscope.modelos.retornoMongo;

import lombok.Data;

@Data
public class Phone {

    private String type;
    private String area;
    private String number;
    private String _class;

    // getters e setters
}
