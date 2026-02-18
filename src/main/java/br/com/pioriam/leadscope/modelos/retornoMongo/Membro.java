package br.com.pioriam.leadscope.modelos.retornoMongo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({
        "nome_membro",
        "empresas",
})
public class Membro {
    private String nome_membro;
    private List<Empresa> empresas;
}
