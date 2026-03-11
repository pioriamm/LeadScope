package br.com.pioriam.leadscope.servicos;

import br.com.pioriam.leadscope.modelos.DTO.CnpjRequest;
import br.com.pioriam.leadscope.modelos.Socio;
import br.com.pioriam.leadscope.modelos.retornoMongo.*;
import br.com.pioriam.leadscope.repositorio.ProspectarRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuditoriaService {

    @Autowired
    ProspectarServiceMongo prospectarServiceMongo;
    @Autowired
    ProsprestarService prosprestarService;

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

    public Map<String, Object> autidotiriaEnriquecimentoLead(String cnpj) {

        List<CnpjRequest> dados = new ArrayList<>();

        CnpjRequest item = new CnpjRequest();
        item.setCnpj(cnpj);

        dados.add(item);

        var result = prosprestarService.buscarDados(dados);
        var empresa = result.get(0);
        return processarEmpresa((Map<String, Object>) empresa);
    }

    public Map<String, Object> processarEmpresa(Map<String, Object> empresa) {

        Set<String> cnpjs = new HashSet<>();
        Set<String> cnaes = new LinkedHashSet<>();
        Map<String, Integer> socios = new LinkedHashMap<>();

        List<Map<String, Object>> membros = (List<Map<String, Object>>) empresa.get("membros");

        if (membros != null) {

            for (Map<String, Object> membro : membros) {

                String nomeSocio = (String) membro.get("nome_membro");

                List<Map<String, Object>> empresas = (List<Map<String, Object>>) membro.get("empresas");

                if (empresas != null) {

                    for (Map<String, Object> emp : empresas) {

                        String cnpj = (String) emp.get("cnpj_empresa_socio");

                        if (cnpj != null) {
                            cnpjs.add(cnpj);
                        }

                        Object cnaeObj = emp.get("cnae");

                        if (cnaeObj != null) {

                            String cnae = cnaeObj.toString();

                            // extrai apenas a descrição
                            if (cnae.contains("text=")) {
                                cnae = cnae.substring(cnae.indexOf("text=") + 5, cnae.length() - 1);
                            }

                            cnaes.add(cnae);
                        }

                        socios.put(nomeSocio, socios.getOrDefault(nomeSocio, 0) + 1);
                    }
                }
            }
        }

        int totalCnpjs = cnpjs.size();
        double valorTotal = totalCnpjs * 150.0;

        Map<String, Object> resultado = new LinkedHashMap<>();

        resultado.put("valorTotal", valorTotal);
        resultado.put("totalCnpjs", totalCnpjs);
        resultado.put("socios", socios);
        resultado.put("cnaes", new ArrayList<>(cnaes));

        return resultado;
    }
}
