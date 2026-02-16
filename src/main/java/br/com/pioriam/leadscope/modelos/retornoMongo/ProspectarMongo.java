package br.com.pioriam.leadscope.modelos.retornoMongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "prospectar")
public class ProspectarMongo {
    @Id
    private String id;

    private List<Dados> dados;

    private String _class;
}
