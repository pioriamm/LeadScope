package br.com.pioriam.leadscope.modelos.retornoMongo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({
        "id_membro",
        "nome_membro",
        "empresas",
})
public class Membro {
    private String id_membro;
    private String nome_membro;
    private List<EmpresaSocio> empresas;
}
