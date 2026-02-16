package br.com.pioriam.leadscope.modelos.retornoMongo;

import lombok.Data;

@Data
public class Status {

    private Integer _id;
    private String text;
    private String _class;

    // getters e setters
}
