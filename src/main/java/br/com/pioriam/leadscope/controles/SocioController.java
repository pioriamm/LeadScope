package br.com.pioriam.leadscope.controles;

import br.com.pioriam.leadscope.modelos.Socio;
import br.com.pioriam.leadscope.servicos.SociosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/socio")
public class SocioController {

    @Autowired
    private SociosService sociosService;

    // =========================
    // CRIAR
    // =========================
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Socio socio) {
        try {
            sociosService.save(socio);
            return ResponseEntity.ok("Sócio cadastrado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // =========================
    // BUSCAR TODOS
    // =========================
    @GetMapping
    public ResponseEntity<List<Socio>> buscarTodosSocios() {
        try {
            List<Socio> lista = sociosService.buscarTodos();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // =========================
    // EDITAR
    // =========================
    @PutMapping
    public ResponseEntity<Void> editar(@RequestBody Socio socio) {
        try {
            sociosService.editar(socio);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // =========================
    // DELETE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            sociosService.deletar(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}