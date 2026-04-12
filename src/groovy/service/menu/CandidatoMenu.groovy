package groovy.service.menu

import groovy.service.LinketinderService

class CandidatoMenu {
    private final LinketinderService service
    private final MenuIO io

    CandidatoMenu(LinketinderService service, MenuIO io) {
        this.service = service
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
                def candidato = service.cadastrarCandidato([
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
                service.listarCandidatos().each { println service.formatarCandidato(it) }
                break
            case 3:
                Integer id = io.lerOpcao("ID do candidato")
                boolean atualizado = service.atualizarCandidato(id, [
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
                boolean deletado = service.deletarCandidato(idDelete)
                println deletado ? "Candidato deletado." : "Candidato nao encontrado."
                break
            default:
                println "Opcao invalida."
        }
    }
}
