/// <reference path="./types.ts" />

function lerInput(id: string): string {
    return (document.getElementById(id) as HTMLInputElement)?.value.trim() || '';
}

function salvarCandidatos(candidatos: CandidatoCadastro[]) {
    localStorage.setItem('candidatos', JSON.stringify(candidatos));
}

function salvarEmpresas(empresas: EmpresaCadastro[]) {
    localStorage.setItem('empresas', JSON.stringify(empresas));
}

function carregarCandidatos(): CandidatoCadastro[] {
    return JSON.parse(localStorage.getItem('candidatos') || '[]');
}

function carregarEmpresas(): EmpresaCadastro[] {
    return JSON.parse(localStorage.getItem('empresas') || '[]');
}
