package br.com.pioriam.leadscope.modelos.retornoMongo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;
@Data
@JsonPropertyOrder({
        "nome_empresa_socio",
        "membros_empresa_socio",
})
public class Empresa {
    private String nome_empresa_socio;
    private List<Person> membros_empresa_socio;
}
