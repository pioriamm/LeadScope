package br.com.pioriam.leadscope.controles;

import br.com.pioriam.leadscope.servicos.ProsprestarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/cnpjja")
public class Prospectar {

    private final ProsprestarService proprestarService;

    public Prospectar(ProsprestarService proprestarService) {
        this.proprestarService = proprestarService;
    }

    @GetMapping("/pesquisar_cnpj")
    public ResponseEntity<String> criarRelacoes(@RequestBody List<Map<String, String>> dados) {

        try {
            proprestarService.buscarDados(dados);
            return ResponseEntity.ok("Done.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao processar requisição: " + e.getMessage());
        }
    }
}
