package br.com.pioriam.leadscope.controles;

import br.com.pioriam.leadscope.modelos.retornoMongo.Dados;
import br.com.pioriam.leadscope.modelos.retornoMongo.ProspectarMongo;
import br.com.pioriam.leadscope.servicos.ProspectarServiceMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/mongo")
public class PesquisaControler {

    @Autowired
    ProspectarServiceMongo prospectarServiceMongo;

    @GetMapping("/buscarDados")
    public ResponseEntity<Page<ProspectarMongo>> buscarDados(Pageable pageable) {

        try {
            Page<ProspectarMongo> resultado =
                    prospectarServiceMongo.buscarDadosMongo(pageable);

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}