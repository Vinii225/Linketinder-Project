import org.junit.jupiter.api.*
import static org.junit.jupiter.api.Assertions.*

class Testes {

    @BeforeEach
    void setup() {
        LinketinderAPP.candidatos = []
        LinketinderAPP.empresas = []
    }

    @Test
    void "Adição Candidato"() {
        def candidato = new Candidato("Teste", "teste@gmail.com", "PB", "58000", "DEV", ["Java"], "123", 20)

        LinketinderAPP.adicionarCandidato(candidato)

        assertEquals(1, LinketinderAPP.candidatos.size())
    }

    @Test
    void "Adição Empresa"() {
        def empresa = new Empresa("Teste", "teste@gmail.com", "PB", "58000", "Suporte", ["BD"], "123", "Brasil")

        LinketinderAPP.adicionarEmpresa(empresa)

        assertEquals(1, LinketinderAPP.empresas.size())
    }
}

