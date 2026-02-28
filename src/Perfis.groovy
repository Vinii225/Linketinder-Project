class Perfil {
    String nome
    String email
    String estado
    String cep
    String descricao
    List<String> competencias = []

    Perfil(String nome, String email, String estado, String cep, String descricao, List<String> competencias) {
        this.nome = nome
        this.email = email
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
        this.competencias = competencias
    }
}

class Candidato extends Perfil {
    String cpf
    int idade

    Candidato(String nome, String email, String estado, String cep, String descricao, List<String> competencias, String cpf, int idade) {
        super(nome, email, estado, cep, descricao, competencias)
        this.cpf = cpf
        this.idade = idade
    }
}

class Empresa extends Perfil {
    String cnpj
    String pais

    Empresa(String nome, String email, String estado, String cep, String descricao, List<String> competencias, String cnpj, String pais) {
        super(nome, email, estado, cep, descricao, competencias)
        this.cnpj = cnpj
        this.pais = pais
    }
}