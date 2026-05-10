package groovy.service.menu

import groovy.controller.CandidatoController
import groovy.service.LinketinderService

class CandidatoMenu {
    private final CandidatoController controller
    private final MenuInput io

    CandidatoMenu(LinketinderService service, MenuInput io) {
        this.controller = new CandidatoController(service)
        this.io = io
    }

    void abrir() {
        println "\n--- CRUD Candidato ---"
        println "1. Cadastrar"
        println "2. Listar"
        println "3. Atualizar"
        println "4. Deletar"

        int opcao = io.lerOpcao()
        switch (opcao) {
            case 1:
                def candidato = controller.create([
                    nome: io.lerTexto("Nome"),
                    sobrenome: io.lerTexto("Sobrenome"),
                    dataNasc: io.lerTexto("Data de nascimento (AAAA-MM-DD)"),
                    email: io.lerTexto("Email"),
                    cpf: io.lerTexto("CPF"),
                    pais: io.lerTexto("Pais"),
                    cep: io.lerTexto("CEP"),
                    descricaoPessoal: io.lerTexto("Descricao pessoal"),
                    senha: io.lerTexto("Senha"),
                    competencias: io.lerTexto("Competencias separadas por virgula")
                ])
                println "Candidato cadastrado com ID ${candidato.idCandidato}."
                break
            case 2:
                controller.list().each { println controller.format(it) }
                break
            case 3:
                Integer id = io.lerOpcao("ID do candidato")
                boolean atualizado = controller.update(id, [
                    nome: io.lerTexto("Nome"),
                    sobrenome: io.lerTexto("Sobrenome"),
                    dataNasc: io.lerTexto("Data de nascimento (AAAA-MM-DD)"),
                    email: io.lerTexto("Email"),
                    cpf: io.lerTexto("CPF"),
                    pais: io.lerTexto("Pais"),
                    cep: io.lerTexto("CEP"),
                    descricaoPessoal: io.lerTexto("Descricao pessoal"),
                    senha: io.lerTexto("Senha"),
                    competencias: io.lerTexto("Competencias separadas por virgula")
                ])
                println atualizado ? "Candidato atualizado." : "Candidato nao encontrado."
                break
            case 4:
                Integer idDelete = io.lerOpcao("ID do candidato")
                boolean deletado = controller.delete(idDelete)
                println deletado ? "Candidato deletado." : "Candidato nao encontrado."
                break
            default:
                println "Opcao invalida."
        }
    }
}
