/// <reference path="../../types.ts" />
/// <reference path="../../validacao.ts" />
/// <reference path="../../storage.ts" />

function setAvisoEmpresa(mensagem: string, tipo: 'erro' | 'sucesso') {
    const aviso = document.getElementById('cadastro-aviso');
    if (!aviso) {
        alert(mensagem);
        return;
    }

    aviso.textContent = mensagem;
    aviso.className = `aviso ${tipo}`;
}

const formEmpresa = document.getElementById('form-empresa') as HTMLFormElement;
formEmpresa?.addEventListener('submit', (e) => {
    e.preventDefault();
    
    const emps = carregarEmpresas();
    const skills = lerInput('e-skills');
    const novaEmpresa: EmpresaCadastro = {
        nome: lerInput('e-nome'),
        cnpj: lerInput('e-cnpj'),
        email: lerInput('e-email'),
        descricao: lerInput('e-descricao'),
        cep: lerInput('e-cep'),
        competencias: parseTags(skills)
    };

    const erro = validarEmpresa(novaEmpresa, skills);
    if (erro) {
        setAvisoEmpresa(`Empresa não cadastrada: ${erro}`, 'erro');
        return;
    }

    emps.push(novaEmpresa);
    salvarEmpresas(emps);
    setAvisoEmpresa('Empresa cadastrada com sucesso!', 'sucesso');
    setTimeout(() => {
        location.href = '../../index.html';
    }, 1200);
});

const cands = carregarCandidatos();
const emps = carregarEmpresas();
renderizarVagas(emps);
renderizarGrafico(cands);

function renderizarVagas(empresas: EmpresaCadastro[]) {
    const lista = document.getElementById('lista-vagas');
    if (!lista) return;

    if (!empresas.length) {
        lista.innerHTML = '<p>Nenhuma vaga disponivel no momento.</p>';
        return;
    }

    lista.innerHTML = empresas.map((empresa) => `
        <div class="card-list">
            <h3>${empresa.nome}</h3>
            <p><strong>Descricao:</strong> ${empresa.descricao}</p>
            <p><strong>Skills desejadas:</strong> ${empresa.competencias.join(', ')}</p>
        </div>
    `).join('');
}

function renderizarGrafico(candLista: CandidatoCadastro[]) {
    const canvas = document.getElementById('skillsChart') as HTMLCanvasElement;
    if (!canvas || typeof (window as any).Chart === 'undefined') return;
    
    const contagem: Record<string, number> = {};
    candLista.forEach(c => {
        c.competencias.forEach(skill => {
            contagem[skill] = (contagem[skill] || 0) + 1;
        });
    });

    new (window as any).Chart(canvas, {
        type: 'bar',
        data: {
            labels: Object.keys(contagem),
            datasets: [{
                label: 'Candidatos por Skill',
                data: Object.values(contagem),
                backgroundColor: '#007bff'
            }]
        },
        options: { scales: { y: { beginAtZero: true } } }
    });
}

const tabela = document.querySelector('#tabela-anonima tbody');
if (tabela) {
    tabela.innerHTML = cands.map(c => `
        <tr>
            <td>${c.competencias.join(', ')}</td>
            <td>${c.descricao}</td>
        </tr>
    `).join('');
}
