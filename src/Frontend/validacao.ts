/// <reference path="./types.ts" />

function validarCandidato(c: CandidatoCadastro, skillsBrutas: string): string | null {
    if (!regex.nome.test(c.nome)) return 'Nome invalido.';
    if (!c.dataNasc) return 'Data de nascimento obrigatoria.';
    if (!regex.email.test(c.email)) return 'Email invalido.';
    if (!regex.cpf.test(c.cpf)) return 'CPF invalido. Use 000.000.000-00';
    if (!regex.cep.test(c.cep)) return 'CEP invalido. Use 00000-000';
    if (!regex.descricao.test(c.descricao)) return 'Descricao muito curta (minimo 5 caracteres).';
    if (!regex.tags.test(skillsBrutas)) return 'Skills invalidas. Use: Java, Python, SQL';
    return null;
}

function validarEmpresa(e: EmpresaCadastro, skillsBrutas: string): string | null {
    if (!regex.nomeEmpresa.test(e.nome)) return 'Nome da empresa invalido.';
    if (!regex.cnpj.test(e.cnpj)) return 'CNPJ invalido. Use 00.000.000/0001-00';
    if (!regex.email.test(e.email)) return 'Email corporativo invalido.';
    if (!regex.descricao.test(e.descricao)) return 'Descricao da empresa muito curta.';
    if (!regex.cep.test(e.cep)) return 'CEP invalido. Use 00000-000';
    if (!regex.tags.test(skillsBrutas)) return 'Competencias invalidas. Use: Java, SQL, Spring';
    return null;
}
