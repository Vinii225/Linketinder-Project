# Linketinder

Sistema de cadastro e visualizacao de candidatos e empresas, com duas frentes:

- Backend em Groovy (terminal) com CRUD em JDBC
- Frontend em TypeScript + HTML/CSS (navegador) modularizado

## Frontend Modularizado (TypeScript)

Estrutura simplificada em módulos:

- `src/Frontend/types.ts`: interfaces e regex compartilhados
- `src/Frontend/validacao.ts`: funções de validação
- `src/Frontend/storage.ts`: carregar/salvar em localStorage
- `src/Frontend/Candidato/Cadastro/cadastro.ts`: lógica de cadastro de candidato
- `src/Frontend/Empresa/Cadastro/cadastro.ts`: lógica de cadastro de empresa + renderização de vagas e gráfico

## Compilar Frontend

```bash
tsc -p tsconfig.frontend.json
```

Gera arquivos em `src/Frontend/dist/`.

## Atributos Cadastro Frontend

**Candidato**: nome, data_nasc, email, cpf, cep, descricao, competencias
**Empresa**: nome, cnpj, email, descricao, cep, competencias

Validações de regex em `src/Frontend/types.ts` e `src/Frontend/validacao.ts`.

## Armazenamento

Dados salvos em localStorage:
- `candidatos`: array de CandidatoCadastro
- `empresas`: array de EmpresaCadastro

## Backend JDBC (Groovy)

Camadas:
- `model`: entidades de dominio
- `data`: DAO com JDBC
- `service`: regras de negocio
- `LinketinderAPP`: menu de CLI

O arquivo `linketinderSQL.sql` contem o banco com 5 candidatos e 5 empresas pre-inseridos.

```bash
./gradlew test
groovy src/LinketinderAPP.groovy
```
