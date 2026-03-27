declare const Chart: any;

interface Perfil {
    nome: string;
    email: string;
    competencias: string[];
}

interface Candidato extends Perfil {
    formacao: string;
}

interface Empresa extends Perfil {
    cnpj: string;
    vagas: string;
}

class LinketinderGerenciador {
    private candidatos: Candidato[] = JSON.parse(localStorage.getItem('candidatos') || '[]');
    private empresas: Empresa[] = JSON.parse(localStorage.getItem('empresas') || '[]');

    constructor() {
        this.inicializarFormularios();
        this.renderizarListas();
        if (document.getElementById('skillsChart')) {
            this.renderizarGrafico();
        }
    }

    private inicializarFormularios() {
        const formC = document.getElementById('form-candidato') as HTMLFormElement;
        formC?.addEventListener('submit', (e) => {
            e.preventDefault();
            const novoCandidato: Candidato = {
                nome: (document.getElementById('c-nome') as HTMLInputElement).value,
                email: (document.getElementById('c-email') as HTMLInputElement).value,
                competencias: (document.getElementById('c-skills') as HTMLInputElement).value.split(','),
                formacao: "Estudante"
            };
            this.candidatos.push(novoCandidato);
            localStorage.setItem('candidatos', JSON.stringify(this.candidatos));
            alert('Candidato cadastrado!');
            location.href = '../../index.html';
        });

        const formE = document.getElementById('form-empresa') as HTMLFormElement;
        formE?.addEventListener('submit', (e) => {
            e.preventDefault();
            const novaEmpresa: Empresa = {
                nome: (document.getElementById('e-nome') as HTMLInputElement).value,
                cnpj: (document.getElementById('e-cnpj') as HTMLInputElement).value,
                email: (document.getElementById('e-email') as HTMLInputElement).value,
                vagas: (document.getElementById('e-vagas') as HTMLInputElement).value,
                competencias: (document.getElementById('e-skills') as HTMLInputElement).value.split(',')
            };
            this.empresas.push(novaEmpresa);
            localStorage.setItem('empresas', JSON.stringify(this.empresas));
            alert('Empresa cadastrada!');
            location.href = '../../index.html';
        });
    }

    private renderizarListas() {
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

    private renderizarGrafico() {
        const contagem: { [key: string]: number } = {};
        this.candidatos.forEach(c => {
            c.competencias.forEach(skill => {
                const s = skill.trim();
                contagem[s] = (contagem[s] || 0) + 1;
            });
        });

        const ctx = document.getElementById('skillsChart') as HTMLCanvasElement;
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