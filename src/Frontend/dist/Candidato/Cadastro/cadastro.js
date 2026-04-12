"use strict";
function setAvisoCandidato(mensagem, tipo) {
    const aviso = document.getElementById('cadastro-aviso');
    if (!aviso) {
        alert(mensagem);
        return;
    }
    aviso.textContent = mensagem;
    aviso.className = `aviso ${tipo}`;
}
const formCandidato = document.getElementById('form-candidato');
formCandidato === null || formCandidato === void 0 ? void 0 : formCandidato.addEventListener('submit', (e) => {
    e.preventDefault();
    const cand = carregarCandidatos();
    const skills = lerInput('c-skills');
    const novoCandidato = {
        nome: lerInput('c-nome'),
        dataNasc: lerInput('c-data-nasc'),
        email: lerInput('c-email'),
        cpf: lerInput('c-cpf'),
        cep: lerInput('c-cep'),
        descricao: lerInput('c-descricao'),
        competencias: parseTags(skills)
    };
    const erro = validarCandidato(novoCandidato, skills);
    if (erro) {
        setAvisoCandidato(`Usuario não cadastrado: ${erro}`, 'erro');
        return;
    }
    cand.push(novoCandidato);
    salvarCandidatos(cand);
    setAvisoCandidato('Candidato cadastrado com sucesso!', 'sucesso');
    setTimeout(() => {
        location.href = '../../index.html';
    }, 1200);
});
