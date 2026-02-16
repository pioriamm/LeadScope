package br.com.pioriam.leadscope.controles;

import br.com.pioriam.leadscope.modelos.retornoMongo.ProspectarMongo;
import br.com.pioriam.leadscope.servicos.ProspectarServiceMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/pesquisar")
public class PesquisaControler {

    @Autowired
    ProspectarServiceMongo prospectarServiceMongo;

    @GetMapping("/buscarDados")
    public ResponseEntity<List<ProspectarMongo>> criarRelacoes() {

        try {
            List<ProspectarMongo> resultado = prospectarServiceMongo.buscarDadosMongo();
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
