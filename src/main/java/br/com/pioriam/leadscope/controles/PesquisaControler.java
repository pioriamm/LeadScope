package br.com.pioriam.leadscope.controles;

import br.com.pioriam.leadscope.modelos.retornoMongo.Dados;
import br.com.pioriam.leadscope.modelos.retornoMongo.ProspectarMongo;
import br.com.pioriam.leadscope.servicos.ProspectarServiceMongo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/mongo")
@Tag(name = "Busca base prospectada")
public class PesquisaControler {

    @Autowired
    ProspectarServiceMongo prospectarServiceMongo;
    @Operation(
            summary = "Retorna todas as empresas da base já prospectada no Cnpjá ",
            description = "Empresa completa."
    )
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