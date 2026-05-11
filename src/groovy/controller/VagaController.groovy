package groovy.controller

import groovy.model.Vaga
import groovy.service.LinketinderService

class VagaController {
    private final LinketinderService service

    VagaController(LinketinderService service) {
        this.service = service
    }

    Vaga create(Map dados) {
        return service.cadastrarVaga(dados)
    }
}
