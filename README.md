# Linketinder - MVP 

O **Linketinder** é um sistema de contratação simplificado
que une o conceito de competências do LinkedIn com a praticidade
de "match" do Tinder. Este projeto é um MVP (Minimum Viable Product).

**Desenvolvedor:** Vinícius Ares

---

## Tecnologias e Conceitos Utilizados

O projeto foi totalmente implementado em **Groovy**, utilizando os seguintes conceitos de programação:

* **POO (Programação Orientada a Objetos):** Uso de Herança, Classes e Interfaces.
* **Encapsulamento:** Organização de atributos em classes específicas.
* **Coleções (Collections):** Uso de `Lists` dinâmicas para gerenciar candidatos e empresas.
* **Estruturas de Dados:** Implementação de herança entre `Perfil`, `Candidato` e `Empresa`.
* **Groovy Power:** Uso de strings interpoladas, closures (`each`) e validação de tipos dinâmica (`isInteger`).

---

## Requisitos do MVP

- Cadastro prévio de no mínimo 5 candidatos.
- Cadastro prévio de no mínimo 5 empresas (focadas na região de João Pessoa - PB).
- Atributos completos (Nome, E-mail, CPF/CNPJ, idade, Estado, CEP e descrição).
- Sistema de competências (Array de skills) para ambos os perfis.
- Menu interativo via terminal.
- Validação de entrada de dados (evitando erros de digitação).

---

## Estrutura do Projeto

O código está organizado para facilitar a manutenção:



* **LinketinderAPP.groovy**: Contém a classe principal, o método `main`, o menu de navegação e a lógica de pré-cadastro.
* **Classes de Modelo**: As classes `Perfil`, `Candidato` e `Empresa` definem a estrutura de dados e herança do sistema.

---

## Como Executar

Para rodar o projeto, você precisa ter o **Groovy** instalado em sua máquina.

1.  Clone o repositório:
    ```bash
    git clone [https://github.com/SEU_USUARIO/linketinder.git](https://github.com/SEU_USUARIO/linketinder.git)
    ```
2.  Navegue até a pasta do projeto:
    ```bash
    cd linketinder
    ```
3.  Execute o arquivo principal:
    ```bash
    groovy LinketinderAPP.groovy
    ```
