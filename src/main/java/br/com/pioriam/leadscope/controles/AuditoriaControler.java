package br.com.pioriam.leadscope.controles;

import br.com.pioriam.leadscope.modelos.DTO.CnpjRequest;
import br.com.pioriam.leadscope.servicos.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Service
@Tag(name = "Auditoria de dados")
@RestController
@RequestMapping("/v1/auditoria")
public class AuditoriaControler {


    @Autowired
    AuditoriaService auditoriaService;

    @Operation(
            summary = "Auditoria dos Dados",
            description = "Retorna os valores auditados do das empresas"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Métricas de auditoria das empresas prospectadas",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Object.class),
                    examples = @ExampleObject(
                            name = "Exemplo de resposta",
                            value = """
                                    {
                                      "totalEmpresas": 120,
                                      "sociosDiretosUnicos": 300,
                                      "sociosIndiretosUnicos": 540,
                                      "totalSocios": 610,
                                      "ticketMedio": 18060.0
                                    }
                                    """
                    )
            )
    )
    @GetMapping()
    public ResponseEntity<Map<String, Object>> auditoria() {

        try {
            Map<String, Object> resultado = auditoriaService.auditoria();
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(
            summary = "Auditoria dos Leads",
            description = "retorna a saude dos lead do cnpj raiz"
    )
    @GetMapping("/enrequecimentoLead")
    public ResponseEntity<Map<String, Object>> enrequecimentoLead(@RequestParam  String cnpj) {

        try {
            Map<String, Object> resultado = auditoriaService.autidotiriaEnriquecimentoLead(cnpj);
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
