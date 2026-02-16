package br.com.pioriam.leadscope.servicos;

import br.com.pioriam.leadscope.modelos.retornoMongo.ProspectarMongo;
import br.com.pioriam.leadscope.repositorio.ProspectarRepositorioMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProspectarServiceMongo {

    private final ProspectarRepositorioMongo prospectarRepositorioMongo;

    public ProspectarServiceMongo(ProspectarRepositorioMongo prospectarRepositorioMongo) {
        this.prospectarRepositorioMongo = prospectarRepositorioMongo;
    }


    public List<ProspectarMongo> buscarDadosMongo (){
        return prospectarRepositorioMongo.findAll();
    }

}
