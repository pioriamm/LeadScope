package br.com.pioriam.leadscope.controles;

import br.com.pioriam.leadscope.modelos.EmpresaConciliadora;
import br.com.pioriam.leadscope.servicos.EmpresaConciliadoraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/empresas-conciliadora")
@Tag(name = "Busca Empresas da Conciliadora")
public class EmpresaConciliadoraControler {


    @Autowired
    EmpresaConciliadoraService empresaConciliadoraService;

    @Operation(
            summary = "Retorna todas as empresas",
            description = "Retorna todas as emprsas do banco."
    )
    @GetMapping
    public ResponseEntity<List<EmpresaConciliadora>> findAll() {
        return ResponseEntity.ok(empresaConciliadoraService.findAll());
    }

//    @PostMapping
//    public ResponseEntity<EmpresaConciliadora> save(@RequestBody EmpresaConciliadora empresa) {
//        EmpresaConciliadora novaEmpresa = empresaConciliadoraService.save(empresa);
//        return ResponseEntity.ok(novaEmpresa);
//    }

    @Operation(
            summary = "Atualiza o status da empres",
            description = "Com o id da empresa e atualizado o campo pesquisado true"
    )
    @PutMapping("/{id}/pesquisado")
    public ResponseEntity<Void> atualizarStatus(@PathVariable String id) {
        empresaConciliadoraService.atualizarStatus(id);
        return ResponseEntity.noContent().build();
    }
}