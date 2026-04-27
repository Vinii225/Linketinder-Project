package groovy.service.menu

import groovy.service.LinketinderService

class CompetenciaMenu {
    private final LinketinderService service
    private final MenuInput io

    CompetenciaMenu(LinketinderService service, MenuInput io) {
        this.service = service
        this.io = io
    }

    void abrir() {
        println "\n--- CRUD Competencia ---"
        println "1. Cadastrar"
        println "2. Listar"
        println "3. Atualizar"
        println "4. Deletar"

        int opcao = io.lerOpcao()
        switch (opcao) {
            case 1:
                def competencia = service.cadastrarCompetencia([
                    nomeCompetencia: io.lerTexto("Nome da competencia")
                ])
                println "Competencia cadastrada com ID ${competencia.idCompetencia}."
                break
            case 2:
                service.listarCompetencias().each { println service.formatarCompetencia(it) }
                break
            case 3:
                Integer id = io.lerOpcao("ID da competencia")
                boolean atualizado = service.atualizarCompetencia(id, [
                    nomeCompetencia: io.lerTexto("Novo nome da competencia")
                ])
                println atualizado ? "Competencia atualizada." : "Competencia nao encontrada."
                break
            case 4:
                Integer idDelete = io.lerOpcao("ID da competencia")
                boolean deletado = service.deletarCompetencia(idDelete)
                println deletado ? "Competencia deletada." : "Competencia nao encontrada."
                break
            default:
                println "Opcao invalida."
        }
    }
}
