package br.com.pioriam.leadscope.modelos;

import lombok.Data;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.List;

@Data
public class Company {

    private List<Member> members;
    private Long id;
    private String socioNome;
    private String name;
}