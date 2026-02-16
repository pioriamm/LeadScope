package br.com.pioriam.leadscope.repositorio;

import br.com.pioriam.leadscope.modelos.retornoMongo.ProspectarMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProspectarRepositorioMongo extends MongoRepository<ProspectarMongo, String> {
}
