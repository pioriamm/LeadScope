package br.com.pioriam.leadscope.modelos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "prospectar")
@Data
public class Prospectar {

    @Id
    private String id;
    private List<Map<String, Object>> dados;

}
