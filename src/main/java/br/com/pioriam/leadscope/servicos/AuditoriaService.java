package br.com.pioriam.leadscope.servicos;

import br.com.pioriam.leadscope.modelos.Socio;
import br.com.pioriam.leadscope.modelos.retornoMongo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuditoriaService {

    @Autowired
    ProspectarServiceMongo prospectarServiceMongo;
    public Map<String, Object> auditoria() {

        List<ProspectarMongo> listaEmpresas = prospectarServiceMongo.buscarDadosMongoSemPaginacao();

        int totalEmpresas = 0;

        Set<String> sociosDiretos = new HashSet<>();
        Set<String> sociosIndiretos = new HashSet<>();
        Set<String> todosSocios = new HashSet<>();

        for (ProspectarMongo prospect : listaEmpresas) {

            totalEmpresas++;

            if (prospect.getDados() == null) continue;

            for (Dados dado : prospect.getDados()) {

                if (dado.getMembros() == null) continue;

                for (Membro membro : dado.getMembros()) {

                    if (membro.getId_membro() != null) {
                        sociosDiretos.add(membro.getId_membro());
                    }

                    if (membro.getEmpresas() == null) continue;

                    for (EmpresaSocio empresa : membro.getEmpresas()) {

                        if (empresa.getMembros_empresa_socio() == null) continue;

                        for (MembroEmpresaSocio socio : empresa.getMembros_empresa_socio()) {

                            if (socio.getId() != null) {
                                sociosIndiretos.add(socio.getId());
                            }
                        }
                    }
                }
            }
        }

        int sociosDiretosUnicos = sociosDiretos.size();
        int sociosIndiretosUnicos = sociosIndiretos.size();


        todosSocios.addAll(sociosDiretos);
        todosSocios.addAll(sociosIndiretos);
        int totalSocios = todosSocios.size();


        double ticketMedio = totalEmpresas * 150.50;

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("totalEmpresas", totalEmpresas);
        resultado.put("sociosDiretosUnicos", sociosDiretosUnicos);
        resultado.put("sociosIndiretosUnicos", sociosIndiretosUnicos);
        resultado.put("totalSocios", totalSocios);
        resultado.put("ticketMedio", ticketMedio);

        return resultado;
    }
}
