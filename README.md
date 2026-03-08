# Linketinder - MVP (Versão 2.0 com TDD)

O Linketinder é um sistema de contratação simplificado que une o conceito de competências do LinkedIn com a praticidade de "match" do Tinder. Esta versão evoluiu de um protótipo estático para um sistema interativo com validação de dados via testes unitários.

**Desenvolvedor:** Vinícius Ares

---

## Novidades desta Versão

* **Cadastro Interativo**: Implementação de menu para inserção de novos candidatos e empresas em tempo real via terminal.
* **Testes Unitários (TDD)**: Criação de suíte de testes utilizando JUnit 5 para validar a inserção de novos elementos nas listas.
* **Métodos de Persistência**: Adição dos métodos adicionarCandidato e adicionarEmpresa para manipulação segura das coleções.

---

## Tecnologias e Conceitos Utilizados

O projeto utiliza o ecossistema Groovy e as melhores práticas de engenharia de software:

* **POO (Programação Orientada a Objetos)**: Uso de herança entre Perfil, Candidato e Empresa.
* **TDD (Test-Driven Development)**: Desenvolvimento orientado a testes para garantir que cada unidade (cadastro) funcione de forma independente.
* **Coleções (Collections)**: Gerenciamento dinâmico de objetos em listas (ArrayList).
* **Validação de Dados**: Uso de métodos como isInteger() para tratar entradas do usuário via Scanner.

---

## Estrutura do Projeto

* **LinketinderAPP.groovy**: Contém a classe principal, o método main com o menu interativo, a lógica de pré-cadastro e os novos métodos de inserção.
* **Perfis.groovy**: Define os modelos de dados Candidato e Empresa que herdam da classe Perfil.
* **Testes.groovy**: Arquivo contendo os testes unitários automatizados para a etapa de cadastro.

---

## Como Executar o App

1. Certifique-se de ter o Groovy instalado em sua máquina.
2. Navegue até a pasta do projeto via terminal.
3. Execute o comando:
   ```bash
   groovy LinketinderAPP.groovy