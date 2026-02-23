package br.com.pioriam.leadscope.controles;

import br.com.pioriam.leadscope.modelos.EmpresaConciliadora;
import br.com.pioriam.leadscope.servicos.EmpresaConciliadoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/empresas-conciliadora")
@CrossOrigin(origins = "*")
public class EmpresaConciliadoraControler {


    @Autowired
    EmpresaConciliadoraService empresaConciliadoraService;

    @GetMapping
    public ResponseEntity<List<EmpresaConciliadora>> findAll() {
        return ResponseEntity.ok(empresaConciliadoraService.findAll());
    }

    @PostMapping
    public ResponseEntity<EmpresaConciliadora> save(@RequestBody EmpresaConciliadora empresa) {
        EmpresaConciliadora novaEmpresa = empresaConciliadoraService.save(empresa);
        return ResponseEntity.ok(novaEmpresa);
    }

    @PutMapping("/{id}/pesquisado")
    public ResponseEntity<Void> atualizarStatus(@PathVariable String id) {
        empresaConciliadoraService.atualizarStatus(id);
        return ResponseEntity.noContent().build();
    }
}