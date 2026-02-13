package br.com.pioriam.leadscope.repositorio;

import br.com.pioriam.leadscope.modelos.Cliente;
import br.com.pioriam.leadscope.modelos.Prospector;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProspectarRepositorio extends MongoRepository<Prospector, String> {
}
