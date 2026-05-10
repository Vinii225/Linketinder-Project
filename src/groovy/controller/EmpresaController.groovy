package groovy.controller

import groovy.service.LinketinderService
import groovy.model.Empresa

class EmpresaController {
    private final LinketinderService service

    EmpresaController(LinketinderService service) {
        this.service = service
    }

    Empresa create(Map dados) {
        return service.cadastrarEmpresa(dados)
    }

    List<Empresa> list() {
        return service.listarEmpresas()
    }

    boolean update(Integer id, Map dados) {
        return service.atualizarEmpresa(id, dados)
    }

    boolean delete(Integer id) {
        return service.deletarEmpresa(id)
    }

    String format(Empresa empresa) {
        return service.formatarEmpresa(empresa)
    }
}
