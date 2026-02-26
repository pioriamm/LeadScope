package br.com.pioriam.leadscope.servicos;

import br.com.pioriam.leadscope.modelos.retornoMongo.ProspectarMongo;
import br.com.pioriam.leadscope.repositorio.ProspectarRepositorioMongo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProspectarServiceMongo {

    private final ProspectarRepositorioMongo prospectarRepositorioMongo;

    public ProspectarServiceMongo(ProspectarRepositorioMongo prospectarRepositorioMongos) {
        this.prospectarRepositorioMongo = prospectarRepositorioMongos;
    }


    public List<ProspectarMongo> buscarDadosMongo (){

        return prospectarRepositorioMongo.findAll();
    }

}
