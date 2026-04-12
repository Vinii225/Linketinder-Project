package groovy.service.menu

import java.util.Scanner
import groovy.service.LinketinderService

class LinketinderMenu {
    private final Scanner scanner = new Scanner(System.in)
    private final MenuIO io = new MenuIO(scanner)
    private final LinketinderService service = new LinketinderService()
    private final CandidatoMenu candidatoMenu = new CandidatoMenu(service, io)
    private final EmpresaMenu empresaMenu = new EmpresaMenu(service, io)
    private final CompetenciaMenu competenciaMenu = new CompetenciaMenu(service, io)
    private final VagaMenu vagaMenu = new VagaMenu(service, io)

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
                switch (io.lerOpcao()) {
                case 1:
                    candidatoMenu.abrir()
                    break
                case 2:
                    empresaMenu.abrir()
                    break
                case 3:
                    competenciaMenu.abrir()
                    break
                case 4:
                    vagaMenu.abrir()
                    break
                case 0:
                    executando = false
                    println "Encerrando aplicacao."
                    break
                default:
                    println "Opcao invalida."
                }
            } catch (Exception exception) {
                println "Erro: ${exception.message}"
            }
        }
    }
}
