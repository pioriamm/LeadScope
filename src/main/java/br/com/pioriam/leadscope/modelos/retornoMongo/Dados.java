package br.com.pioriam.leadscope.modelos.retornoMongo;

import lombok.Data;

import java.util.List;
@Data
public class Dados {

    private Integer compania_id;
    private String cnpj_raiz_id;
    private String empresa_raiz;
    private String alias;

    private List<Email> email;
    private List<Phone> telefone;
    private Status status;
    private List<Membro> membros;
    private List<EmpresaSocio> empresas;
    private List<Person> membros_empresa_socio;

    // getters e setters
}

