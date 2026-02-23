package br.com.pioriam.leadscope.servicos;

import br.com.pioriam.leadscope.modelos.EmpresaConciliadora;
import br.com.pioriam.leadscope.repositorio.EmpresaConciliadoraRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaConciliadoraService {

    @Autowired
    EmpresaConciliadoraRepositorio empresaConciliadoraRepositorio;

    public List<EmpresaConciliadora> findAll() {
        return  empresaConciliadoraRepositorio.findAll();
    }

    public EmpresaConciliadora save(EmpresaConciliadora empresa) {
        return empresaConciliadoraRepositorio.save(empresa);
    }

    public  void atualizarStatus (String id){
        EmpresaConciliadora empresa = empresaConciliadoraRepositorio
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        empresa.setPesquisado(true);
        empresaConciliadoraRepositorio.save(empresa);
    }
}
