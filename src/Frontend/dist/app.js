"use strict";
class LinketinderGerenciador {
    constructor() {
        this.candidatos = JSON.parse(localStorage.getItem('candidatos') || '[]');
        this.empresas = JSON.parse(localStorage.getItem('empresas') || '[]');
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
            const novoCandidato = {
                nome: document.getElementById('c-nome').value,
                email: document.getElementById('c-email').value,
                competencias: document.getElementById('c-skills').value.split(','),
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
            const novaEmpresa = {
                nome: document.getElementById('e-nome').value,
                cnpj: document.getElementById('e-cnpj').value,
                email: document.getElementById('e-email').value,
                vagas: document.getElementById('e-vagas').value,
                competencias: document.getElementById('e-skills').value.split(',')
            };
            this.empresas.push(novaEmpresa);
            localStorage.setItem('empresas', JSON.stringify(this.empresas));
            alert('Empresa cadastrada!');
            location.href = '../../index.html';
        });
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
