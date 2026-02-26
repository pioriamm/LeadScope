package br.com.pioriam.leadscope.repositorio;

import br.com.pioriam.leadscope.modelos.EmpresaConciliadora;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaConciliadoraRepositorio extends MongoRepository<EmpresaConciliadora, String> {
    boolean existsByCnpj(String cnpj);
}
