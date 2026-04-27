package groovy.service

import groovy.model.Candidato
import groovy.model.Competencia
import groovy.model.Empresa
import groovy.model.Vaga

interface EntityFormatter {
    String formatarCandidato(Candidato candidato)
    String formatarEmpresa(Empresa empresa)
    String formatarCompetencia(Competencia competencia)
    String formatarVaga(Vaga vaga)
}
