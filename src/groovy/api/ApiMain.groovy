package groovy.api

import com.sun.net.httpserver.HttpServer
import groovy.controller.CandidatoController
import groovy.controller.EmpresaController
import groovy.controller.VagaController
import groovy.service.LinketinderService

import java.net.InetSocketAddress
import java.util.concurrent.Executors

class ApiMain {
    static void main(String[] args) {
        int port = (System.getenv('PORT') ?: '8080') as int

        def service = new LinketinderService()
        def candidatoController = new CandidatoController(service)
        def empresaController = new EmpresaController(service)
        def vagaController = new VagaController(service)

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0)
        server.executor = Executors.newFixedThreadPool(8)

        server.createContext('/candidatos') { exchange ->
            try {
                if (exchange.requestMethod != 'POST') {
                    JsonHttp.metodoNaoPermitido(exchange, ['POST'])
                    return
                }

                Map dados = JsonHttp.readJsonObject(exchange)
                def candidato = candidatoController.create(dados)
                JsonHttp.criado(exchange, [
                    idCandidato      : candidato.idCandidato,
                    nome             : candidato.nome,
                    sobrenome        : candidato.sobrenome,
                    dataNasc         : candidato.dataNasc?.toString(),
                    email            : candidato.email,
                    cpf              : candidato.cpf,
                    pais             : candidato.pais,
                    cep              : candidato.cep,
                    descricaoPessoal : candidato.descricaoPessoal,
                    competencias     : candidato.competencias ?: []
                ])
            } catch (IllegalArgumentException e) {
                JsonHttp.badRequest(exchange, e.message)
            } catch (Exception e) {
                System.err.println("[API] Erro em /candidatos: ${e.class.simpleName}: ${e.message}")
                JsonHttp.erroInterno(exchange)
            }
        }

        server.createContext('/empresas') { exchange ->
            try {
                if (exchange.requestMethod != 'POST') {
                    JsonHttp.metodoNaoPermitido(exchange, ['POST'])
                    return
                }

                Map dados = JsonHttp.readJsonObject(exchange)
                def empresa = empresaController.create(dados)
                JsonHttp.criado(exchange, [
                    idEmpresa        : empresa.idEmpresa,
                    nomeEmpresa      : empresa.nomeEmpresa,
                    cnpj             : empresa.cnpj,
                    emailCorporativo : empresa.emailCorporativo,
                    descricaoEmpresa : empresa.descricaoEmpresa,
                    pais             : empresa.pais,
                    cep              : empresa.cep
                ])
            } catch (IllegalArgumentException e) {
                JsonHttp.badRequest(exchange, e.message)
            } catch (Exception e) {
                System.err.println("[API] Erro em /empresas: ${e.class.simpleName}: ${e.message}")
                JsonHttp.erroInterno(exchange)
            }
        }

        server.createContext('/vagas') { exchange ->
            try {
                if (exchange.requestMethod != 'POST') {
                    JsonHttp.metodoNaoPermitido(exchange, ['POST'])
                    return
                }

                Map dados = JsonHttp.readJsonObject(exchange)
                def vaga = vagaController.create(dados)
                JsonHttp.criado(exchange, [
                    idVaga      : vaga.idVaga,
                    idEmpresa   : vaga.idEmpresa,
                    nomeVaga    : vaga.nomeVaga,
                    descricao   : vaga.descricao,
                    localizacao : vaga.localizacao
                ])
            } catch (IllegalArgumentException e) {
                JsonHttp.badRequest(exchange, e.message)
            } catch (Exception e) {
                System.err.println("[API] Erro em /vagas: ${e.class.simpleName}: ${e.message}")
                JsonHttp.erroInterno(exchange)
            }
        }

        server.start()
        println("[API] API REST do Linketinder em http://localhost:${port}")
        println('[API] Rotas: POST /candidatos | POST /empresas | POST /vagas')
    }
}
