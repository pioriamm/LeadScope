package br.com.pioriam.leadscope.servicos;

import br.com.pioriam.leadscope.modelos.retornoMongo.Dados;
import br.com.pioriam.leadscope.modelos.retornoMongo.ProspectarMongo;
import br.com.pioriam.leadscope.repositorio.ProspectarRepositorioMongo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProspectarServiceMongo {

    private final ProspectarRepositorioMongo prospectarRepositorioMongo;

    public ProspectarServiceMongo(ProspectarRepositorioMongo prospectarRepositorioMongos) {
        this.prospectarRepositorioMongo = prospectarRepositorioMongos;
    }


    public Page<ProspectarMongo> buscarDadosMongo(Pageable pageable) {
        return prospectarRepositorioMongo.findAll(pageable);
    }

}
