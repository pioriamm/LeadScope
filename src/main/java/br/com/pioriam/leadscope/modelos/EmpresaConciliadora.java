package br.com.pioriam.leadscope.modelos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "empresas_conciliadora")
@Data
public class EmpresaConciliadora {

    @Id
    private String id;
    private String razaoSocial;
    private String alias;
    private String cnpj;
    private boolean pesquisado;

}
