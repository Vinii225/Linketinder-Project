class Perfil {
    String nome
    String email
    String estado
    String cep
    String descricao
    List<String> competencias = []
}

class Candidato extends Perfil {
    String cpf
    int idade
}

class Empresa extends Perfil {
    String cnpj
    String pais
}
