# Linketinder

Sistema de cadastro e visualizacao de candidatos e empresas, com duas frentes:

- Backend em Groovy (terminal) com CRUD em JDBC
- Frontend em TypeScript + HTML/CSS (navegador) modularizado

## Frontend Modularizado (TypeScript)

Estrutura simplificada em módulos:

- `src/Frontend/types.ts`: interfaces e regex compartilhados
- `src/Frontend/validacao.ts`: funções de validação
- `src/Frontend/storage.ts`: carregar/salvar em localStorage
- `src/Frontend/groovy.model.Candidato/Cadastro/cadastro.ts`: lógica de cadastro de candidato
- `src/Frontend/groovy.model.Empresa/Cadastro/cadastro.ts`: lógica de cadastro de empresa + renderização de vagas e gráfico

## Compilar Frontend

```bash
tsc -p tsconfig.frontend.json
```

Gera arquivos em `src/Frontend/dist/`.

## Atributos Cadastro Frontend

**groovy.model.Candidato**: nome, data_nasc, email, cpf, cep, descricao, competencias
**groovy.model.Empresa**: nome, cnpj, email, descricao, cep, competencias

Validações de regex em `src/Frontend/types.ts` e `src/Frontend/validacao.ts`.

## Armazenamento

Dados salvos em localStorage:

- `candidatos`: array de CandidatoCadastro
- `empresas`: array de EmpresaCadastro

## Backend JDBC (Groovy)

Camadas:

- `src/groovy/Main.groovy`: ponto de entrada (apenas inicia o menu)
- `src/groovy/model`: entidades de dominio (`Candidato`, `Empresa`, `Vaga`, `Competencia`)
- `src/groovy/model/data`: conexao JDBC e DAOs com CRUD
- `src/groovy/service`: regras de negocio e validacoes
- `src/groovy/service/menu`: menu principal e submenus por entidade

CRUDs implementados:

- Candidato (com relacionamento N:N com competencia via `candidato_competencia`)
- Empresa
- Competencia
- Vaga (relacionamento 1:N de empresa para vaga)

O arquivo `linketinderSQL.sql` contem o banco com 5 candidatos e 5 empresas pre-inseridos.

```bash
./gradlew test
groovy src/groovy/Main.groovy
```
