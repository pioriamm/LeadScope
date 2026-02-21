package br.com.pioriam.leadscope.servicos;

import br.com.pioriam.leadscope.modelos.Socio;
import br.com.pioriam.leadscope.repositorio.SocioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SociosService {

    @Autowired
    SocioRepositorio socioRepositorio;

    public Socio save(Socio socio) {
        if (socioRepositorio.existsByCpf(socio.getCpf())) {
            throw new RuntimeException("Sócio já está cadastrado com este CPF.");
        }

        return socioRepositorio.save(socio);
    }

    public Socio buscarPorId(String id) {

        return socioRepositorio.findById(id).get();
    }

    public List<Socio> buscarTodos(){
        return socioRepositorio.findAll();
    }

    public Socio editar(Socio socio){

        Socio socioEditado = socioRepositorio.findById(socio.getId()).get();

        socioEditado.setNome(socio.getNome());
        socioEditado.setCpf(socio.getCpf());
        socioEditado.setEmail(socio.getEmail());
        socioEditado.setTelefone(socio.getTelefone());

        return socioRepositorio.save(socioEditado);
    }

    public void deletar(String id) {
        socioRepositorio.deleteById(id);
    }
}
