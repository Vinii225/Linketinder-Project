"use strict";
class LinketinderGerenciador {
    constructor() {
        this.candidatos = JSON.parse(localStorage.getItem('candidatos') || '[]');
        this.empresas = JSON.parse(localStorage.getItem('empresas') || '[]');
        this.regex = {
            nomeCompleto: /^[A-Za-zÀ-ÿ]+(?:[ '-][A-Za-zÀ-ÿ]+)+$/,
            nomeEmpresa: /^[A-Za-zÀ-ÿ0-9]+(?:[ .,'&-][A-Za-zÀ-ÿ0-9]+)*$/,
            email: /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/,
            cnpj: /^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/,
            vaga: /^(?=.{5,120}$)[A-Za-zÀ-ÿ0-9 ,./()-]+$/,
            tags: /^\s*[A-Za-zÀ-ÿ0-9#+.-]{2,}(?:\s*,\s*[A-Za-zÀ-ÿ0-9#+.-]{2,})*\s*$/
        };
        this.inicializarFormularios();
        this.renderizarListas();
        if (document.getElementById('skillsChart')) {
            this.renderizarGrafico();
        }
    }
    inicializarFormularios() {
        const formC = document.getElementById('form-candidato');
        formC === null || formC === void 0 ? void 0 : formC.addEventListener('submit', (e) => {
            e.preventDefault();
            const nome = document.getElementById('c-nome').value.trim();
            const email = document.getElementById('c-email').value.trim();
            const skills = document.getElementById('c-skills').value.trim();
            const erroCandidato = this.validarCandidato(nome, email, skills);
            if (erroCandidato) {
                alert(erroCandidato);
                return;
            }
            const novoCandidato = {
                nome,
                email,
                competencias: this.parseTags(skills),
                formacao: "Estudante"
            };
            this.candidatos.push(novoCandidato);
            localStorage.setItem('candidatos', JSON.stringify(this.candidatos));
            alert('Candidato cadastrado!');
            location.href = '../../index.html';
        });
        const formE = document.getElementById('form-empresa');
        formE === null || formE === void 0 ? void 0 : formE.addEventListener('submit', (e) => {
            e.preventDefault();
            const nome = document.getElementById('e-nome').value.trim();
            const cnpj = document.getElementById('e-cnpj').value.trim();
            const email = document.getElementById('e-email').value.trim();
            const vagas = document.getElementById('e-vagas').value.trim();
            const skills = document.getElementById('e-skills').value.trim();
            const erroEmpresa = this.validarEmpresa(nome, cnpj, email, vagas, skills);
            if (erroEmpresa) {
                alert(erroEmpresa);
                return;
            }
            const novaEmpresa = {
                nome,
                cnpj,
                email,
                vagas,
                competencias: this.parseTags(skills)
            };
            this.empresas.push(novaEmpresa);
            localStorage.setItem('empresas', JSON.stringify(this.empresas));
            alert('Empresa cadastrada!');
            location.href = '../../index.html';
        });
    }
    parseTags(tags) {
        return tags.split(',').map((tag) => tag.trim()).filter(Boolean);
    }
    validarCandidato(nome, email, skills) {
        if (!this.regex.nomeCompleto.test(nome)) {
            return 'Nome invalido. Informe nome e sobrenome (apenas letras).';
        }
        if (!this.regex.email.test(email)) {
            return 'E-mail invalido. Use o formato nome@dominio.com.';
        }
        if (!this.regex.tags.test(skills)) {
            return 'Skills invalidas. Use tags separadas por virgula (ex: Java, Groovy, SQL).';
        }
        return null;
    }
    validarEmpresa(nome, cnpj, email, vaga, skills) {
        if (!this.regex.nomeEmpresa.test(nome)) {
            return 'Nome da empresa invalido.';
        }
        if (!this.regex.cnpj.test(cnpj)) {
            return 'CNPJ invalido. Use o formato 00.000.000/0001-00.';
        }
        if (!this.regex.email.test(email)) {
            return 'E-mail corporativo invalido. Use o formato rh@empresa.com.';
        }
        if (!this.regex.vaga.test(vaga)) {
            return 'Descricao da vaga invalida. Use entre 5 e 120 caracteres.';
        }
        if (!this.regex.tags.test(skills)) {
            return 'Competencias invalidas. Use tags separadas por virgula (ex: Java, SQL, Spring).';
        }
        return null;
    }
    renderizarListas() {
        const listaVagas = document.getElementById('lista-vagas');
        if (listaVagas) {
            listaVagas.innerHTML = this.empresas.map(e => `
                <div class="card-list">
                    <h3>Vaga: ${e.vagas}</h3>
                    <p><strong>Requisitos:</strong> ${e.competencias.join(', ')}</p>
                    <p><strong>Empresa:</strong> [Oculto]</p>
                    <button class="btn-small">Tenho Interesse</button>
                </div>
            `).join('');
        }
        const tabelaC = document.querySelector('#tabela-anonima tbody');
        if (tabelaC) {
            tabelaC.innerHTML = this.candidatos.map(c => `
                <tr>
                    <td>${c.competencias.join(', ')}</td>
                    <td>${c.formacao}</td>
                </tr>
            `).join('');
        }
    }
    renderizarGrafico() {
        const contagem = {};
        this.candidatos.forEach(c => {
            c.competencias.forEach(skill => {
                const s = skill.trim();
                contagem[s] = (contagem[s] || 0) + 1;
            });
        });
        const ctx = document.getElementById('skillsChart');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: Object.keys(contagem),
                datasets: [{
                        label: 'Quantidade de Candidatos por Skill',
                        data: Object.values(contagem),
                        backgroundColor: '#007bff'
                    }]
            },
            options: { scales: { y: { beginAtZero: true } } }
        });
    }
}
new LinketinderGerenciador();
