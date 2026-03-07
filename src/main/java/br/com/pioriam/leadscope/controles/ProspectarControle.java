package br.com.pioriam.leadscope.controles;

import br.com.pioriam.leadscope.modelos.DTO.CnpjRequest;
import br.com.pioriam.leadscope.servicos.ProsprestarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Prospecção de Empresas (CNPJA)")
@RestController
@RequestMapping("/v1/cnpjja")
public class ProspectarControle {

    @Autowired
    ProsprestarService proprestarService;


    @Operation(
            summary = "Consultar empresas e sócios a partir do CNPJ",
            description = "Recebe uma lista de CNPJs válidos, consulta os dados empresariais através da API CNPJA e retorna as informações da empresa, incluindo sócios e empresas vinculadas aos sócios."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Empresas e sócios relacionados ao CNPJ informado",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Object.class),
                    examples = @ExampleObject(
                            name = "Exemplo de resposta",
                            value = """
                                [
                                  {
                                    "compania_id": 12345678,
                                    "cnpj_raiz_id": "12345678000199",
                                    "empresa_raiz": "EMPRESA EXEMPLO TECNOLOGIA LTDA",
                                    "alias": "Tech Solutions",
                                    "email": [],
                                    "telefone": [
                                      {
                                        "area": "31",
                                        "number": "33334444",
                                        "type": "LANDLINE"
                                      }
                                    ],
                                    "status": {
                                      "id": 2,
                                      "text": "Ativa"
                                    },
                                    "cnae": {
                                      "id": 6201501,
                                      "text": "Desenvolvimento de software"
                                    },
                                    "eConciliadora": true,
                                    "ativoConciliadora": false,
                                    "membros": [
                                      {
                                        "id_membro": "11111111-2222-3333-4444-555555555555",
                                        "nome_membro": "Carlos Andrade",
                                        "empresas": []
                                      }
                                    ]
                                  }
                                ]
                                """
                    )
            )
    )
    @PostMapping("popularBase")
    public ResponseEntity<List<Map<String, Object>>> criarRelacoes(@RequestBody List<CnpjRequest> dados) {

        try {
            List<Map<String, Object>> resultado = proprestarService.buscarDados(dados);
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}