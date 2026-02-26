package br.com.pioriam.leadscope.modelos.retornoMongo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({
        "compania_id",
        "cnpj_raiz_id",
        "empresa_raiz",
        "eConciliadora",
        "alias",
        "email",
        "telefone",
        "status",
        "cnae",
        "membros"
})
public class Dados {

 private Integer compania_id;
 private String cnpj_raiz_id;
 private String empresa_raiz;
 private String alias;
 private boolean eConciliadora;
 private List<Email> email;
 private List<Phone> telefone;
 private Status status;
 private Cnae cnae;

 private List<Membro> membros;
}