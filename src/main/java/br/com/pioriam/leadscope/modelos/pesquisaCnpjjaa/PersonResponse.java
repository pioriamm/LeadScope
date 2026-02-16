package br.com.pioriam.leadscope.modelos.pesquisaCnpjjaa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonResponse {

    private String id;
    private String name;
    private String age;

    private List<Membership> membership;
}