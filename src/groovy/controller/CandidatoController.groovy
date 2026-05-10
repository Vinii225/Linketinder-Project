package groovy.controller

import groovy.service.LinketinderService
import groovy.model.Candidato

class CandidatoController {
    private final LinketinderService service

    CandidatoController(LinketinderService service) {
        this.service = service
    }

    Candidato create(Map dados) {
        return service.cadastrarCandidato(dados)
    }

    List<Candidato> list() {
        return service.listarCandidatos()
    }

    boolean update(Integer id, Map dados) {
        return service.atualizarCandidato(id, dados)
    }

    boolean delete(Integer id) {
        return service.deletarCandidato(id)
    }

    String format(Candidato candidato) {
        return service.formatarCandidato(candidato)
    }
}
