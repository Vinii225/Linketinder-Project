package groovy.service

interface ValidationService {
    String validarObrigatorio(Object valor, String campo)
    Integer validarNumero(Object valor, String campo)
    List<String> normalizarCompetencias(Object valor)
    void aplicarCamposObrigatorios(def destino, Map dados, Map<String, String> campos)
}
