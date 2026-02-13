package br.com.pioriam.leadscope.controles;

import br.com.pioriam.leadscope.servicos.ProsprestarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/cnpjja")
public class Prospectar {

    private final ProsprestarService proprestarService;

    public Prospectar(ProsprestarService proprestarService) {
        this.proprestarService = proprestarService;
    }

    @GetMapping("/pesquisar_cnpj")
    public ResponseEntity<String> criarRelacoes(@RequestBody Map<String, String> cnpj) {

        try {
            proprestarService.buscarDados(cnpj);
            return ResponseEntity.ok("Processamento concluído.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao processar requisição: " + e.getMessage());
        }
    }
}
