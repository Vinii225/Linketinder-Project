package groovy.api

import com.sun.net.httpserver.HttpExchange

import java.nio.charset.StandardCharsets

class JsonHttp {
    static Map readJsonObject(HttpExchange exchange) {
        String contentType = exchange.requestHeaders.getFirst('Content-Type') ?: ''
        if (!contentType.toLowerCase().contains('application/json')) {
            throw new IllegalArgumentException('Content-Type deve ser application/json.')
        }

        String body = exchange.requestBody.getText(StandardCharsets.UTF_8.name())
        if (!body || body.trim().isEmpty()) {
            throw new IllegalArgumentException('Body JSON nao pode ser vazio.')
        }

        try {
            def slurper = Class.forName('groovy.json.JsonSlurper').getDeclaredConstructor().newInstance()
            def parsed = slurper.parseText(body)

            if (!(parsed instanceof Map)) {
                throw new IllegalArgumentException('JSON deve ser um objeto.')
            }

            return (Map) parsed
        } catch (IllegalArgumentException e) {
            throw e
        } catch (Exception e) {
            throw new IllegalArgumentException('JSON invalido.')
        }
    }

    static void criado(HttpExchange exchange, Object body) {
        respond(exchange, 201, body)
    }

    static void badRequest(HttpExchange exchange, String message) {
        respond(exchange, 400, [error: message ?: 'Requisicao invalida.'])
    }

    static void metodoNaoPermitido(HttpExchange exchange, List<String> allow) {
        exchange.responseHeaders.set('Allow', allow.join(', '))
        respond(exchange, 405, [error: 'Metodo nao permitido.'])
    }

    static void erroInterno(HttpExchange exchange) {
        respond(exchange, 500, [error: 'Erro interno do servidor.'])
    }

    static void respond(HttpExchange exchange, int status, Object body) {
        def jsonOutputClass = Class.forName('groovy.json.JsonOutput')
        String json = (String) jsonOutputClass.toJson(body)
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8)
        exchange.responseHeaders.set('Content-Type', 'application/json; charset=utf-8')
        exchange.sendResponseHeaders(status, bytes.length)
        exchange.responseBody.withCloseable { it.write(bytes) }
    }
}
