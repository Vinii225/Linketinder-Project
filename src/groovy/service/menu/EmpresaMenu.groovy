package groovy.service.menu

import groovy.service.LinketinderService

class EmpresaMenu {
    private final LinketinderService service
    private final MenuInput io

    EmpresaMenu(LinketinderService service, MenuInput io) {
        this.service = service
        this.io = io
    }

    void abrir() {
        println "\n--- CRUD Empresa ---"
        println "1. Cadastrar"
        println "2. Listar"
        println "3. Atualizar"
        println "4. Deletar"

        int opcao = io.lerOpcao()
        switch (opcao) {
            case 1:
                def empresa = service.cadastrarEmpresa([
                    nomeEmpresa: io.lerTexto("Nome da empresa"),
                    cnpj: io.lerTexto("CNPJ"),
                    emailCorporativo: io.lerTexto("Email corporativo"),
                    descricaoEmpresa: io.lerTexto("Descricao da empresa"),
                    pais: io.lerTexto("Pais"),
                    cep: io.lerTexto("CEP"),
                    senha: io.lerTexto("Senha")
                ])
                println "Empresa cadastrada com ID ${empresa.idEmpresa}."
                break
            case 2:
                service.listarEmpresas().each { println service.formatarEmpresa(it) }
                break
            case 3:
                Integer id = io.lerOpcao("ID da empresa")
                boolean atualizado = service.atualizarEmpresa(id, [
                    nomeEmpresa: io.lerTexto("Nome da empresa"),
                    cnpj: io.lerTexto("CNPJ"),
                    emailCorporativo: io.lerTexto("Email corporativo"),
                    descricaoEmpresa: io.lerTexto("Descricao da empresa"),
                    pais: io.lerTexto("Pais"),
                    cep: io.lerTexto("CEP"),
                    senha: io.lerTexto("Senha")
                ])
                println atualizado ? "Empresa atualizada." : "Empresa nao encontrada."
                break
            case 4:
                Integer idDelete = io.lerOpcao("ID da empresa")
                boolean deletado = service.deletarEmpresa(idDelete)
                println deletado ? "Empresa deletada." : "Empresa nao encontrada."
                break
            default:
                println "Opcao invalida."
        }
    }
}
