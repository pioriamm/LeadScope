package br.com.pioriam.leadscope.modelos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "socio")
public class Socio {
    @Id
    private String id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
}
