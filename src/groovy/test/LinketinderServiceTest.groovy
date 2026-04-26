package groovy.service

import groovy.data.CandidatoDAO
import groovy.data.CompetenciaDAO
import groovy.data.EmpresaDAO
import groovy.data.VagaDAO
import groovy.model.Candidato
import groovy.model.Empresa
import org.junit.jupiter.api.Test

import java.time.LocalDate

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertTrue

class LinketinderServiceTest {

    @Test
    void atualizaCandidatoComCamposNormalizados() {
        def candidatoDAO = new StubCandidatoDAO(
            candidatoExistente: new Candidato(
                idCandidato: 7,
                nome: 'Antigo',
                sobrenome: 'Nome',
                dataNasc: LocalDate.parse('1990-01-01'),
                email: 'antigo@exemplo.com',
                cpf: '000',
                pais: 'BR',
                cep: '00000',
                descricaoPessoal: 'Antiga descricao',
                senha: 'senha',
                competencias: ['Java']
            )
        )
        def service = new LinketinderService(candidatoDAO, new EmpresaDAO(), new CompetenciaDAO(), new VagaDAO())

        boolean atualizado = service.atualizarCandidato(7, [
            nome: ' Ana ',
            sobrenome: ' Silva ',
            dataNasc: '2001-02-03',
            email: ' ana@exemplo.com ',
            cpf: '12345678900',
            pais: ' Brasil ',
            cep: ' 88000000 ',
            descricaoPessoal: ' Dev ',
            senha: ' segredo ',
            competencias: ' Java , Groovy , SQL '
        ])

        assertTrue(atualizado)
        assertEquals('Ana', candidatoDAO.candidatoAtualizado.nome)
        assertEquals('Silva', candidatoDAO.candidatoAtualizado.sobrenome)
        assertEquals(LocalDate.parse('2001-02-03'), candidatoDAO.candidatoAtualizado.dataNasc)
        assertEquals('ana@exemplo.com', candidatoDAO.candidatoAtualizado.email)
        assertEquals(['Java', 'Groovy', 'SQL'], candidatoDAO.candidatoAtualizado.competencias)
    }

    @Test
    void atualizarEmpresaRetornaFalseQuandoNaoExisteRegistro() {
        def empresaDAO = new StubEmpresaDAO(empresaExistente: null)
        def service = new LinketinderService(new CandidatoDAO(), empresaDAO, new CompetenciaDAO(), new VagaDAO())

        boolean atualizado = service.atualizarEmpresa(99, [
            nomeEmpresa: 'Empresa',
            cnpj: '123',
            emailCorporativo: 'contato@exemplo.com',
            descricaoEmpresa: 'Descricao',
            pais: 'Brasil',
            cep: '88000000',
            senha: 'segredo'
        ])

        assertFalse(atualizado)
        assertEquals(null, empresaDAO.empresaAtualizada)
    }

    @Test
    void validarNumeroRejeitaTextoNaoNumerico() {
        def exception = assertThrows(IllegalArgumentException) {
            LinketinderService.validarNumero('abc', 'ID da empresa')
        }

        assertEquals('ID da empresa deve ser numerico.', exception.message)
    }

    private static class StubCandidatoDAO extends CandidatoDAO {
        Candidato candidatoExistente
        Candidato candidatoAtualizado

        @Override
        Candidato findById(Integer idCandidato) {
            return candidatoExistente
        }

        @Override
        boolean update(Candidato candidato) {
            candidatoAtualizado = candidato
            return true
        }
    }

    private static class StubEmpresaDAO extends EmpresaDAO {
        Empresa empresaExistente
        Empresa empresaAtualizada

        @Override
        Empresa findById(Integer idEmpresa) {
            return empresaExistente
        }

        @Override
        boolean update(Empresa empresa) {
            empresaAtualizada = empresa
            return true
        }
    }
}
