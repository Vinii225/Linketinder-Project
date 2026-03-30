# Linketinder

Sistema de cadastro e visualizacao de candidatos e empresas, com duas frentes:

- Backend em Groovy (modo terminal)
- Frontend em TypeScript + HTML/CSS (modo navegador)

**Desenvolvedor:** Vinicius Ares

## Visao Geral

O projeto combina conceitos de vagas e competencias em um fluxo simples de cadastro.

- No terminal (Groovy), ha menu interativo para listar e adicionar candidatos/empresas.
- No frontend (TypeScript), os dados sao salvos no `localStorage` e renderizados nas paginas.

## Tecnologias

- Groovy 5
- JUnit 5 (testes)
- TypeScript
- HTML/CSS

## Estrutura Principal

- `src/LinketinderAPP.groovy`: ponto de entrada do app em terminal.
- `src/Perfis.groovy`: classes de dominio (`Perfil`, `Candidato`, `Empresa`).
- `src/Testes.groovy`: testes unitarios da logica de insercao.
- `src/Frontend/app.ts`: logica principal do frontend (cadastro, listagem e grafico).
- `src/Frontend/dist/app.js`: build gerado do TypeScript.

## Como Executar (Groovy)

1. Tenha o Groovy instalado (ou configurado na IDE).
2. Na raiz do projeto, execute:

```bash
groovy src/LinketinderAPP.groovy
```

## Frontend TypeScript

Compile o frontend com:

```bash
tsc -p tsconfig.frontend.json
```

Arquivo gerado:

- `src/Frontend/dist/app.js`

As paginas HTML em `src/Frontend/` ja estao configuradas para carregar esse build.

## Validacoes Regex no Frontend

Os formularios de cadastro do frontend possuem validacoes com Regex em `src/Frontend/app.ts`.
Quando um campo e invalido, o envio e bloqueado e uma mensagem de erro e exibida via `alert`.

### Candidato (`form-candidato`)

- `Nome Completo` (`c-nome`): exige nome e sobrenome, com letras e separadores comuns.
- `E-mail` (`c-email`): exige formato valido de e-mail.
- `Suas Skills` (`c-skills`): exige tags separadas por virgula.

Exemplo valido:

- Nome: `Ana Souza`
- E-mail: `ana.souza@email.com`
- Skills: `Java, Groovy, SQL`

### Empresa (`form-empresa`)

- `Nome da Empresa` (`e-nome`): valida nome empresarial com letras/numeros.
- `CNPJ` (`e-cnpj`): exige formato `00.000.000/0001-00`.
- `E-mail Corporativo` (`e-email`): exige formato valido de e-mail.
- `Descricao da Vaga` (`e-vagas`): aceita entre 5 e 120 caracteres validos.
- `Competencias Desejadas` (`e-skills`): exige tags separadas por virgula.

Exemplo valido:

- Nome: `Tech Solutions LTDA`
- CNPJ: `12.345.678/0001-90`
- E-mail: `rh@techsolutions.com`
- Vaga: `Desenvolvedor Java Pleno`
- Competencias: `Java, SQL, Spring`

### Observacao

Campos citados na atividade (CPF, telefone, LinkedIn, CEP etc.) nao foram adicionados porque ainda nao existem nos formularios atuais do projeto.

## Observacoes Importantes

- A listagem de vagas da tela de candidato e dinamica: nao ha vagas fixas no HTML.
- As vagas exibidas sao apenas as cadastradas durante o uso e armazenadas em `localStorage`.
- Se quiser "zerar" os dados exibidos no frontend, limpe o `localStorage` no navegador.

## Testes

O projeto possui testes unitarios em `src/Testes.groovy` para validar insercao de candidatos e empresas.
Eles podem ser executados pela IDE (JUnit 5) com o classpath do projeto configurado.