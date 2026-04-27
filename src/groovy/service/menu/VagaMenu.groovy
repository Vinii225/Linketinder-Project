package groovy.service.menu

import groovy.service.LinketinderService

class VagaMenu {
    private final LinketinderService service
    private final MenuInput io

    VagaMenu(LinketinderService service, MenuInput io) {
        this.service = service
        this.io = io
    }

    void abrir() {
        println "\n--- CRUD Vaga ---"
        println "1. Cadastrar"
        println "2. Listar"
        println "3. Atualizar"
        println "4. Deletar"

        int opcao = io.lerOpcao()
        switch (opcao) {
            case 1:
                def vaga = service.cadastrarVaga([
                    idEmpresa: io.lerTexto("ID da empresa"),
                    nomeVaga: io.lerTexto("Nome da vaga"),
                    descricao: io.lerTexto("Descricao"),
                    localizacao: io.lerTexto("Localizacao")
                ])
                println "Vaga cadastrada com ID ${vaga.idVaga}."
                break
            case 2:
                service.listarVagas().each { println service.formatarVaga(it) }
                break
            case 3:
                Integer id = io.lerOpcao("ID da vaga")
                boolean atualizado = service.atualizarVaga(id, [
                    idEmpresa: io.lerTexto("ID da empresa"),
                    nomeVaga: io.lerTexto("Nome da vaga"),
                    descricao: io.lerTexto("Descricao"),
                    localizacao: io.lerTexto("Localizacao")
                ])
                println atualizado ? "Vaga atualizada." : "Vaga nao encontrada."
                break
            case 4:
                Integer idDelete = io.lerOpcao("ID da vaga")
                boolean deletado = service.deletarVaga(idDelete)
                println deletado ? "Vaga deletada." : "Vaga nao encontrada."
                break
            default:
                println "Opcao invalida."
        }
    }
}
