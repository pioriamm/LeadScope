package br.com.pioriam.leadscope.modelos.retornoMongo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@JsonPropertyOrder({
        "id",
        "dados",
})
@Document(collection = "prospectar")
public class ProspectarMongo {
    @Id
    private String id;
    private List<Dados> dados;
}
