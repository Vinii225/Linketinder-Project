package groovy.service

import groovy.model.Candidato
import groovy.model.Competencia
import groovy.model.Empresa
import groovy.model.Vaga
import groovy.data.CandidatoDAO
import groovy.data.CompetenciaDAO
import groovy.data.EmpresaDAO
import groovy.data.VagaDAO

import java.time.LocalDate

class LinketinderService {
    private final CandidatoDAO candidatoDAO
    private final EmpresaDAO empresaDAO
    private final CompetenciaDAO competenciaDAO
    private final VagaDAO vagaDAO

    LinketinderService() {
        this(new CandidatoDAO(), new EmpresaDAO(), new CompetenciaDAO(), new VagaDAO())
    }

    LinketinderService(
        CandidatoDAO candidatoDAO,
        EmpresaDAO empresaDAO,
        CompetenciaDAO competenciaDAO,
        VagaDAO vagaDAO
    ) {
        this.candidatoDAO = candidatoDAO
        this.empresaDAO = empresaDAO
        this.competenciaDAO = competenciaDAO
        this.vagaDAO = vagaDAO
    }

    Candidato cadastrarCandidato(Map dados) {
        Candidato candidato = new Candidato(
            nome: validarObrigatorio(dados.nome, "Nome"),
            sobrenome: validarObrigatorio(dados.sobrenome, "Sobrenome"),
            dataNasc: LocalDate.parse(validarObrigatorio(dados.dataNasc, "Data de nascimento")),
            email: validarObrigatorio(dados.email, "Email"),
            cpf: validarObrigatorio(dados.cpf, "CPF"),
            pais: validarObrigatorio(dados.pais, "Pais"),
            cep: validarObrigatorio(dados.cep, "CEP"),
            descricaoPessoal: validarObrigatorio(dados.descricaoPessoal, "Descricao pessoal"),
            senha: validarObrigatorio(dados.senha, "Senha"),
            competencias: normalizarCompetencias(dados.competencias)
        )
        return candidatoDAO.create(candidato)
    }

    boolean atualizarCandidato(Integer idCandidato, Map dados) {
        Candidato existente = candidatoDAO.findById(idCandidato)
        if (!existente) {
            return false
        }

        aplicarCamposObrigatorios(existente, dados, [
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
        existente.competencias = normalizarCompetencias(dados.competencias)

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
            nomeEmpresa: validarObrigatorio(dados.nomeEmpresa, "Nome da empresa"),
            cnpj: validarObrigatorio(dados.cnpj, "CNPJ"),
            emailCorporativo: validarObrigatorio(dados.emailCorporativo, "Email corporativo"),
            descricaoEmpresa: validarObrigatorio(dados.descricaoEmpresa, "Descricao da empresa"),
            pais: validarObrigatorio(dados.pais, "Pais"),
            cep: validarObrigatorio(dados.cep, "CEP"),
            senha: validarObrigatorio(dados.senha, "Senha")
        )
        return empresaDAO.create(empresa)
    }

    boolean atualizarEmpresa(Integer idEmpresa, Map dados) {
        Empresa existente = empresaDAO.findById(idEmpresa)
        if (!existente) {
            return false
        }

        aplicarCamposObrigatorios(existente, dados, [
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
            nomeCompetencia: validarObrigatorio(dados.nomeCompetencia, "Nome da competencia")
        )
        return competenciaDAO.create(competencia)
    }

    boolean atualizarCompetencia(Integer idCompetencia, Map dados) {
        Competencia competencia = new Competencia(
            idCompetencia: idCompetencia,
            nomeCompetencia: validarObrigatorio(dados.nomeCompetencia, "Nome da competencia")
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
            idEmpresa: validarNumero(dados.idEmpresa, "ID da empresa"),
            nomeVaga: validarObrigatorio(dados.nomeVaga, "Nome da vaga"),
            descricao: validarObrigatorio(dados.descricao, "Descricao da vaga"),
            localizacao: validarObrigatorio(dados.localizacao, "Localizacao")
        )
        return vagaDAO.create(vaga)
    }

    boolean atualizarVaga(Integer idVaga, Map dados) {
        Vaga existente = vagaDAO.findById(idVaga)
        if (!existente) {
            return false
        }

        existente.idEmpresa = validarNumero(dados.idEmpresa, "ID da empresa")
        existente.nomeVaga = validarObrigatorio(dados.nomeVaga, "Nome da vaga")
        existente.descricao = validarObrigatorio(dados.descricao, "Descricao da vaga")
        existente.localizacao = validarObrigatorio(dados.localizacao, "Localizacao")
        return vagaDAO.update(existente)
    }

    boolean deletarVaga(Integer idVaga) {
        return vagaDAO.delete(idVaga)
    }

    List<Vaga> listarVagas() {
        return vagaDAO.findAll()
    }

    String formatarCandidato(Candidato candidato) {
        String skills = candidato.competencias ? candidato.competencias.join(", ") : "-"
        return "Candidato(id=${candidato.idCandidato}, nome='${candidato.nome}', email='${candidato.email}', cpf='${candidato.cpf}', competencias=[${skills}])"
    }

    String formatarEmpresa(Empresa empresa) {
        return "Empresa(id=${empresa.idEmpresa}, nome='${empresa.nomeEmpresa}', cnpj='${empresa.cnpj}', email='${empresa.emailCorporativo}')"
    }

    String formatarCompetencia(Competencia competencia) {
        return "Competencia(id=${competencia.idCompetencia}, nome='${competencia.nomeCompetencia}')"
    }

    String formatarVaga(Vaga vaga) {
        return "Vaga(id=${vaga.idVaga}, idEmpresa=${vaga.idEmpresa}, nome='${vaga.nomeVaga}', local='${vaga.localizacao}')"
    }

    static String validarObrigatorio(Object valor, String campo) {
        if (valor == null || valor.toString().trim().isEmpty()) {
            throw new IllegalArgumentException("${campo} nao pode ser nulo ou vazio.")
        }
        return valor.toString().trim()
    }

    static Integer validarNumero(Object valor, String campo) {
        String texto = validarObrigatorio(valor, campo)
        if (!texto.isInteger()) {
            throw new IllegalArgumentException("${campo} deve ser numerico.")
        }
        return texto.toInteger()
    }

    static List<String> normalizarCompetencias(Object valor) {
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

    private static void aplicarCamposObrigatorios(def destino, Map dados, Map<String, String> campos) {
        campos.each { String propriedade, String rotulo ->
            if (propriedade == "dataNasc") { destino."$propriedade" = LocalDate.parse(validarObrigatorio(dados."$propriedade", rotulo)) } else { destino."$propriedade" = validarObrigatorio(dados."$propriedade", rotulo) }
        }
    }
}
