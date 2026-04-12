package groovy.model

import java.time.LocalDate

class Candidato {
    Integer idCandidato
    String nome
    String sobrenome
    LocalDate dataNasc
    String email
    String cpf
    String pais
    String cep
    String descricaoPessoal
    String senha
    List<String> competencias = []
}
