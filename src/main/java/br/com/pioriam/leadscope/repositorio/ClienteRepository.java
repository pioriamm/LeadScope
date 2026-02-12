package br.com.pioriam.leadscope.repositorio;

import br.com.pioriam.leadscope.modelos.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClienteRepository extends MongoRepository<Cliente, String> {
}
