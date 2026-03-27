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

## Observacoes Importantes

- A listagem de vagas da tela de candidato e dinamica: nao ha vagas fixas no HTML.
- As vagas exibidas sao apenas as cadastradas durante o uso e armazenadas em `localStorage`.
- Se quiser "zerar" os dados exibidos no frontend, limpe o `localStorage` no navegador.

## Testes

O projeto possui testes unitarios em `src/Testes.groovy` para validar insercao de candidatos e empresas.
Eles podem ser executados pela IDE (JUnit 5) com o classpath do projeto configurado.