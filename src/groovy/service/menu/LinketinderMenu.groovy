package groovy.service.menu

import java.util.Scanner
import groovy.service.LinketinderService

class LinketinderMenu {
    private final Scanner scanner = new Scanner(System.in)
    private final MenuInput io = new MenuIO(scanner)
    private final LinketinderService service = new LinketinderService()
    private final CandidatoMenu candidatoMenu = new CandidatoMenu(service, io)
    private final EmpresaMenu empresaMenu = new EmpresaMenu(service, io)
    private final CompetenciaMenu competenciaMenu = new CompetenciaMenu(service, io)
    private final VagaMenu vagaMenu = new VagaMenu(service, io)
    private final Map<Integer, Closure<Boolean>> acoes

    LinketinderMenu() {
        this.acoes = criarAcoes()
    }

    void iniciar() {
        boolean executando = true

        while (executando) {
            println "\nLINKETINDER"
            println "1. CRUD Candidato"
            println "2. CRUD Empresa"
            println "3. CRUD Competencia"
            println "4. CRUD Vaga"
            println "0. Sair"

            try {
                Integer opcao = io.lerOpcao()
                Closure<Boolean> acao = acoes[opcao]

                if (!acao) {
                    println "Opcao invalida."
                    continue
                }

                executando = acao.call()
            } catch (Exception exception) {
                println "Erro: ${exception.message}"
            }
        }
    }

    private Map<Integer, Closure<Boolean>> criarAcoes() {
        return [
            1: { candidatoMenu.abrir(); return true },
            2: { empresaMenu.abrir(); return true },
            3: { competenciaMenu.abrir(); return true },
            4: { vagaMenu.abrir(); return true },
            0: {
                println "Encerrando aplicacao."
                return false
            }
        ]
    }
}
