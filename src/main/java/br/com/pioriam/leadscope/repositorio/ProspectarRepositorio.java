package br.com.pioriam.leadscope.repositorio;

import br.com.pioriam.leadscope.modelos.Prospectar;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface ProspectarRepositorio extends MongoRepository<Prospectar, String> {
}
