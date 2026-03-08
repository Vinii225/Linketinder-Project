class LinketinderAPP {
    static List<Candidato> candidatos = []
    static List<Empresa> empresas = []

    static void main(String[] args) {
        // dev : Vinícius Ares

        preCadastrarDados()

        Scanner keyboard = new Scanner(System.in)
        int opcao = 0

        while (opcao != 5) {
            println "\nLinketinder"
            println "1. Listar candidatos"
            println "2. Listar empresas"
            println "3. Adicionar candidato"
            println "4. Adicionar empresa"
            println "5. Sair"
            print "Escolha uma opção: "
            String entrada = keyboard.next()

            if (entrada.isInteger()) {
                opcao = entrada.toInteger()

                switch (opcao) {
                    case 1:
                        println "\nCandidatos cadastrados:"
                        candidatos.each { println "Nome: ${it.nome} | Skills: ${it.competencias}" }
                        break
                    case 2:
                        println "\nEmpresas cadastradas:"
                        empresas.each { println "Empresa: ${it.nome} | Busca: ${it.competencias}" }
                        break
                    case 3:
                        println "Cadastro de Candidato"
                        print "Nome: "
                        String nome = keyboard.next()

                        print "Email: ";
                        String email = keyboard.next()

                        def novoC = new Candidato(nome, email, "PB", "58000", "Dev", ["Groovy"], "123", 20)
                        adicionarCandidato(novoC)
                        println "Candidato cadastrado!"
                        break
                    case 4:
                        println "Cadastro de Empresa"
                        print "Nome: "
                        String nome = keyboard.next()

                        print "Email: "
                        String email = keyboard.next()

                        def novaE = new Empresa(nome, email, "PB", "58000", "Suporte", ["BD"], "123", "Brasil")
                        adicionarEmpresa(novaE)
                        println "Empresa cadastrada!"
                        break
                    case 5:
                        println "Saindo..."
                        break
                    default:
                        println "Opção não existe!"
                        break
                }
            } else {
                println "Erro: Você deve digitar um número válido!"
                opcao = 0
            }
        }
    }

    static void preCadastrarDados() {
        candidatos << new Candidato("Vinicius", "vini@email.com", "RN", "59000", "Dev Java", ["Java", "Groovy", "Spring"], "123.456.789-00", 25)
        candidatos << new Candidato("Laura", "laura@email.com", "SP", "01000", "Front-end", ["Angular", "CSS"], "111.222.333-44", 22)
        candidatos << new Candidato("Luana", "lu@email.com", "RJ", "20000", "Data Science", ["Python", "SQL"], "555.666.777-88", 28)
        candidatos << new Candidato("Pedro", "pe@email.com", "MG", "30000", "Mobile", ["Flutter", "Dart"], "999.888.777-66", 30)
        candidatos << new Candidato("Ana", "ana@email.com", "SC", "88000", "Fullstack", ["Java", "Angular"], "000.111.222-33", 26)

        empresas << new Empresa("Brastex", "rh@brastex.com", "PB", "58076", "Têxtil", ["Logística", "Java"], "00.001", "Brasil")
        empresas << new Empresa("Suconor", "vagas@suconor.com", "PB", "58082", "Bebidas", ["Controle", "Python"], "00.002", "Brasil")
        empresas << new Empresa("Norfil", "jobs@norfil.com", "PB", "58081", "Fiação", ["Engenharia", "SQL"], "00.003", "Brasil")
        empresas << new Empresa("Copobras", "contato@copobras.com", "PB", "58082", "Embalagens", ["Design", "Vendas"], "00.004", "Brasil")
        empresas << new Empresa("TechJampa", "jampa@tech.com", "PB", "58000", "TI", ["Groovy", "Spring"], "00.005", "Brasil")
    }

    static void adicionarCandidato(Candidato candidato) {
        candidatos << candidato
    }

    static void adicionarEmpresa(Empresa empresa) {
        empresas << empresa
    }
}