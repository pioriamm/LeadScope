package br.com.pioriam.leadscope.modelos.pesquisaCnpjjaa;

import lombok.Data;

@Data
public class Address {

    private Integer municipality;
    private String street;
    private String number;
    private String district;
    private String city;
    private String state;
    private String details;
    private String zip;
    private Country country;
}
