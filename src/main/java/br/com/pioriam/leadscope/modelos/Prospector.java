package br.com.pioriam.leadscope.modelos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("prospector")
public class Prospector {
    @Id
    private String id;
    private String nome;
}
