package groovy.service

import groovy.model.Candidato
import groovy.model.Competencia
import groovy.model.Empresa
import groovy.model.Vaga
import groovy.data.CandidatoDAO
import groovy.data.CompetenciaDAO
import groovy.data.EmpresaDAO
import groovy.data.VagaDAO
import groovy.data.contracts.CandidatoRepository
import groovy.data.contracts.CompetenciaRepository
import groovy.data.contracts.EmpresaRepository
import groovy.data.contracts.VagaRepository

import java.time.LocalDate

class LinketinderService {
    private final CandidatoRepository candidatoDAO
    private final EmpresaRepository empresaDAO
    private final CompetenciaRepository competenciaDAO
    private final VagaRepository vagaDAO
    private final ValidationService validator
    private final EntityFormatter formatter

    LinketinderService() {
        this(
            new CandidatoDAO(),
            new EmpresaDAO(),
            new CompetenciaDAO(),
            new VagaDAO(),
            new LinketinderValidator(),
            new LinketinderFormatter()
        )
    }

    LinketinderService(
        CandidatoRepository candidatoDAO,
        EmpresaRepository empresaDAO,
        CompetenciaRepository competenciaDAO,
        VagaRepository vagaDAO
    ) {
        this(
            candidatoDAO,
            empresaDAO,
            competenciaDAO,
            vagaDAO,
            new LinketinderValidator(),
            new LinketinderFormatter()
        )
    }

    LinketinderService(
        CandidatoRepository candidatoDAO,
        EmpresaRepository empresaDAO,
        CompetenciaRepository competenciaDAO,
        VagaRepository vagaDAO,
        ValidationService validator,
        EntityFormatter formatter
    ) {
        this.candidatoDAO = candidatoDAO
        this.empresaDAO = empresaDAO
        this.competenciaDAO = competenciaDAO
        this.vagaDAO = vagaDAO
        this.validator = validator
        this.formatter = formatter
    }

    Candidato cadastrarCandidato(Map dados) {
        Candidato candidato = new Candidato(
            nome: validator.validarObrigatorio(dados.nome, "Nome"),
            sobrenome: validator.validarObrigatorio(dados.sobrenome, "Sobrenome"),
            dataNasc: LocalDate.parse(validator.validarObrigatorio(dados.dataNasc, "Data de nascimento")),
            email: validator.validarObrigatorio(dados.email, "Email"),
            cpf: validator.validarObrigatorio(dados.cpf, "CPF"),
            pais: validator.validarObrigatorio(dados.pais, "Pais"),
            cep: validator.validarObrigatorio(dados.cep, "CEP"),
            descricaoPessoal: validator.validarObrigatorio(dados.descricaoPessoal, "Descricao pessoal"),
            senha: validator.validarObrigatorio(dados.senha, "Senha"),
            competencias: validator.normalizarCompetencias(dados.competencias)
        )
        return candidatoDAO.create(candidato)
    }

    boolean atualizarCandidato(Integer idCandidato, Map dados) {
        Candidato existente = candidatoDAO.findById(idCandidato)
        if (!existente) {
            return false
        }

        validator.aplicarCamposObrigatorios(existente, dados, [
            nome: "Nome",
            sobrenome: "Sobrenome",
            dataNasc: "Data de nascimento",
            email: "Email",
            cpf: "CPF",
            pais: "Pais",
            cep: "CEP",
            descricaoPessoal: "Descricao pessoal",
            senha: "Senha"
        ])
        existente.competencias = validator.normalizarCompetencias(dados.competencias)

        return candidatoDAO.update(existente)
    }

    boolean deletarCandidato(Integer idCandidato) {
        return candidatoDAO.delete(idCandidato)
    }

    List<Candidato> listarCandidatos() {
        return candidatoDAO.findAll()
    }

    Empresa cadastrarEmpresa(Map dados) {
        Empresa empresa = new Empresa(
            nomeEmpresa: validator.validarObrigatorio(dados.nomeEmpresa, "Nome da empresa"),
            cnpj: validator.validarObrigatorio(dados.cnpj, "CNPJ"),
            emailCorporativo: validator.validarObrigatorio(dados.emailCorporativo, "Email corporativo"),
            descricaoEmpresa: validator.validarObrigatorio(dados.descricaoEmpresa, "Descricao da empresa"),
            pais: validator.validarObrigatorio(dados.pais, "Pais"),
            cep: validator.validarObrigatorio(dados.cep, "CEP"),
            senha: validator.validarObrigatorio(dados.senha, "Senha")
        )
        return empresaDAO.create(empresa)
    }

    boolean atualizarEmpresa(Integer idEmpresa, Map dados) {
        Empresa existente = empresaDAO.findById(idEmpresa)
        if (!existente) {
            return false
        }

        validator.aplicarCamposObrigatorios(existente, dados, [
            nomeEmpresa: "Nome da empresa",
            cnpj: "CNPJ",
            emailCorporativo: "Email corporativo",
            descricaoEmpresa: "Descricao da empresa",
            pais: "Pais",
            cep: "CEP",
            senha: "Senha"
        ])

        return empresaDAO.update(existente)
    }

    boolean deletarEmpresa(Integer idEmpresa) {
        return empresaDAO.delete(idEmpresa)
    }

    List<Empresa> listarEmpresas() {
        return empresaDAO.findAll()
    }

    Competencia cadastrarCompetencia(Map dados) {
        Competencia competencia = new Competencia(
            nomeCompetencia: validator.validarObrigatorio(dados.nomeCompetencia, "Nome da competencia")
        )
        return competenciaDAO.create(competencia)
    }

    boolean atualizarCompetencia(Integer idCompetencia, Map dados) {
        Competencia competencia = new Competencia(
            idCompetencia: idCompetencia,
            nomeCompetencia: validator.validarObrigatorio(dados.nomeCompetencia, "Nome da competencia")
        )
        return competenciaDAO.update(competencia)
    }

    boolean deletarCompetencia(Integer idCompetencia) {
        return competenciaDAO.delete(idCompetencia)
    }

    List<Competencia> listarCompetencias() {
        return competenciaDAO.findAll()
    }

    Vaga cadastrarVaga(Map dados) {
        Vaga vaga = new Vaga(
            idEmpresa: validator.validarNumero(dados.idEmpresa, "ID da empresa"),
            nomeVaga: validator.validarObrigatorio(dados.nomeVaga, "Nome da vaga"),
            descricao: validator.validarObrigatorio(dados.descricao, "Descricao da vaga"),
            localizacao: validator.validarObrigatorio(dados.localizacao, "Localizacao")
        )
        return vagaDAO.create(vaga)
    }

    boolean atualizarVaga(Integer idVaga, Map dados) {
        Vaga existente = vagaDAO.findById(idVaga)
        if (!existente) {
            return false
        }

        existente.idEmpresa = validator.validarNumero(dados.idEmpresa, "ID da empresa")
        existente.nomeVaga = validator.validarObrigatorio(dados.nomeVaga, "Nome da vaga")
        existente.descricao = validator.validarObrigatorio(dados.descricao, "Descricao da vaga")
        existente.localizacao = validator.validarObrigatorio(dados.localizacao, "Localizacao")
        return vagaDAO.update(existente)
    }

    boolean deletarVaga(Integer idVaga) {
        return vagaDAO.delete(idVaga)
    }

    List<Vaga> listarVagas() {
        return vagaDAO.findAll()
    }

    String formatarCandidato(Candidato candidato) {
        return formatter.formatarCandidato(candidato)
    }

    String formatarEmpresa(Empresa empresa) {
        return formatter.formatarEmpresa(empresa)
    }

    String formatarCompetencia(Competencia competencia) {
        return formatter.formatarCompetencia(competencia)
    }

    String formatarVaga(Vaga vaga) {
        return formatter.formatarVaga(vaga)
    }

    static String validarObrigatorio(Object valor, String campo) {
        return new LinketinderValidator().validarObrigatorio(valor, campo)
    }

    static Integer validarNumero(Object valor, String campo) {
        return new LinketinderValidator().validarNumero(valor, campo)
    }

    static List<String> normalizarCompetencias(Object valor) {
        return new LinketinderValidator().normalizarCompetencias(valor)
    }
}
