package groovy.service

import groovy.model.Candidato
import groovy.model.Competencia
import groovy.model.Empresa
import groovy.model.Vaga

class LinketinderFormatter implements EntityFormatter {

    String formatarCandidato(Candidato candidato) {
        String skills = candidato.competencias ? candidato.competencias.join(", ") : "-"
        return "Candidato(id=${candidato.idCandidato}, nome='${candidato.nome}', email='${candidato.email}', cpf='${candidato.cpf}', competencias=[${skills}])"
    }

    String formatarEmpresa(Empresa empresa) {
        return "Empresa(id=${empresa.idEmpresa}, nome='${empresa.nomeEmpresa}', cnpj='${empresa.cnpj}', email='${empresa.emailCorporativo}')"
    }

    String formatarCompetencia(Competencia competencia) {
        return "Competencia(id=${competencia.idCompetencia}, nome='${competencia.nomeCompetencia}')"
    }

    String formatarVaga(Vaga vaga) {
        return "Vaga(id=${vaga.idVaga}, idEmpresa=${vaga.idEmpresa}, nome='${vaga.nomeVaga}', local='${vaga.localizacao}')"
    }
}
