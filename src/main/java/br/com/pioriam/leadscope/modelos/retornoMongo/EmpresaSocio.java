package br.com.pioriam.leadscope.modelos.retornoMongo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({
        "id_empresa_socio",
        "nome_empresa_socio",
        "cnpj_empresa_socio",
        "email",
        "telefone",
        "status",
        "cnae",
        "membros_empresa_socio",
})
public class EmpresaSocio {
    private String id_empresa_socio;
    private String nome_empresa_socio;
    private String cnpj_empresa_socio;
    private List<MembroEmpresaSocio> membros_empresa_socio;
    private List<Phone> telefone;
    private List<Email> email;
    private Status status;
    private Cnae cnae;
}
