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

- `src/groovy/Main.groovy`: ponto de entrada (apenas inicia o menu)
- `src/groovy/model`: entidades de dominio (`Candidato`, `Empresa`, `Vaga`, `Competencia`)
- `src/groovy/data`: conexao JDBC e DAOs com CRUD
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

## Resumo da Refatoração

A refatoração do Linketinder focou em melhorar o código existente sem alterar regras de negócio, aplicando princípios de Clean Code como redução de duplicação (DRY), funções menores e maior clareza de nomes. No backend, o service e os DAOs foram reorganizados para reduzir repetição e melhorar legibilidade/manutenção, com validação por testes unitários e build estável via Gradle. O frontend foi mantido, pois já estava modularizado e adequado aos critérios da atividade.

## Autor

Nome: [preencher seu nome]
