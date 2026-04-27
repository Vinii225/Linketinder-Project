package groovy.service

import java.time.LocalDate

class LinketinderValidator implements ValidationService {

    String validarObrigatorio(Object valor, String campo) {
        if (valor == null || valor.toString().trim().isEmpty()) {
            throw new IllegalArgumentException("${campo} nao pode ser nulo ou vazio.")
        }
        return valor.toString().trim()
    }

    Integer validarNumero(Object valor, String campo) {
        String texto = validarObrigatorio(valor, campo)
        if (!texto.isInteger()) {
            throw new IllegalArgumentException("${campo} deve ser numerico.")
        }
        return texto.toInteger()
    }

    List<String> normalizarCompetencias(Object valor) {
        if (valor == null) {
            return []
        }

        if (valor instanceof List) {
            return valor.collect { it?.toString()?.trim() }
                .findAll { it }
        }

        return valor.toString()
            .split(",")
            .collect { it.trim() }
            .findAll { !it.isEmpty() }
    }

    void aplicarCamposObrigatorios(def destino, Map dados, Map<String, String> campos) {
        campos.each { String propriedade, String rotulo ->
            if (propriedade == "dataNasc") {
                destino."$propriedade" = LocalDate.parse(validarObrigatorio(dados."$propriedade", rotulo))
                return
            }
            destino."$propriedade" = validarObrigatorio(dados."$propriedade", rotulo)
        }
    }
}
