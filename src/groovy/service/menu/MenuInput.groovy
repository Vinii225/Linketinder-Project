package groovy.service.menu

interface MenuInput {
    String lerTexto(String rotulo)
    Integer lerOpcao()
    Integer lerOpcao(String rotulo)
}
