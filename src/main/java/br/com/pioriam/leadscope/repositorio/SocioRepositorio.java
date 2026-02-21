package br.com.pioriam.leadscope.repositorio;

import br.com.pioriam.leadscope.modelos.Socio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocioRepositorio extends MongoRepository<Socio, String> {
    List<Socio> getSociosById(String id);
    Optional<Socio> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
