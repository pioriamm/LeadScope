package br.com.pioriam.leadscope.modelos.pesquisaCnpjjaa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Office {

    private String taxId;
    private String company;
    private Boolean head;

}
