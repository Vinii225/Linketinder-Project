package groovy.service.menu

import java.util.Scanner

class MenuIO {
    private final Scanner scanner

    MenuIO(Scanner scanner) {
        this.scanner = scanner
    }

    String lerTexto(String rotulo) {
        print "${rotulo}: "
        return scanner.nextLine()
    }

    Integer lerOpcao() {
        return lerOpcao("Escolha")
    }

    Integer lerOpcao(String rotulo) {
        print "${rotulo}: "
        String entrada = scanner.nextLine()
        if (!entrada.isInteger()) {
            throw new IllegalArgumentException("Valor invalido. Informe um numero.")
        }
        return entrada.toInteger()
    }
}
