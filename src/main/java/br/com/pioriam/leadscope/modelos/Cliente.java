package br.com.pioriam.leadscope.modelos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("clientes")
@Data
public class Cliente {

    @Id
    private String id;
    private String nome;
}

