package br.com.pioriam.leadscope.modelos.retornoMongo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
@Data

@JsonPropertyOrder({
        "compania_id",
        "cnpj_raiz_id",
        "empresa_raiz",
        "alias",
        "email",
        "telefone",
        "status",
        "membros",
        "empresas"
})
public class Dados {

   // private Integer compania_id;
    private String cnpj_raiz_id;
    private String empresa_raiz;
    //private String alias;
    private List<Email> email;
    private List<Phone> telefone;
    private Status status;
    private List<Membro> membros;
    private List<EmpresaSocio> empresas;

}

