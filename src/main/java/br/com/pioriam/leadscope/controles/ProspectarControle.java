package br.com.pioriam.leadscope.controles;

import br.com.pioriam.leadscope.servicos.ProsprestarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/cnpjja")
public class ProspectarControle {

    private final ProsprestarService proprestarService;

    public ProspectarControle(ProsprestarService proprestarService) {
        this.proprestarService = proprestarService;
    }

    @PostMapping("pesquisar_cnpj")
    public ResponseEntity<List<Map<String, Object>>> criarRelacoes(
            @RequestBody List<Map<String, String>> dados) {

        try {
            List<Map<String, Object>> resultado =
                    proprestarService.buscarDados(dados);

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
