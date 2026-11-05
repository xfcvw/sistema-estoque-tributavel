# Sistema de Gerenciamento de Estoque Tributável

Projeto acadêmico desenvolvido em Java para funcionar no terminal. O sistema controla funcionários, clientes, produtos e movimentações de estoque, além de apresentar uma estimativa didática de tributação.

## Funcionalidades

- cadastro e listagem de funcionários;
- cadastro e listagem de clientes por CPF;
- cadastro de produtos;
- registro de entrada e saída de estoque;
- consulta do estoque completo e dos itens no estoque mínimo;
- relatório do valor armazenado e do tributo estimado;
- escolha do regime tributário;
- validação de CPF, códigos, quantidades, preços e opções do menu;
- modo de teste para conferir as principais regras do sistema.

## Estrutura do repositório

| Caminho | Conteúdo |
| --- | --- |
| `src/Main.java` | Código principal em Java |
| `pseudocodigo/` | Versão do algoritmo em Portugol |
| `docs/DOCUMENTACAO.md` | Contexto, objetivos, regras e explicações |
| `docs/fluxograma/` | Fluxograma em Draw.io e PDF |
| `docs/relatorio/` | Documento acadêmico em Word |
| `docs/apresentacao/` | Apresentação do projeto em PowerPoint |

## Como executar

É necessário ter o JDK 17 ou superior instalado.

```bash
javac -d out src/Main.java
java -cp out Main
```

Para executar os testes de robustez:

```bash
java -cp out Main --teste
```

## Persistencia dos dados

Os cadastros, o estoque e o regime tributario sao salvos automaticamente em `estoque.txt`, no diretorio em que o programa e executado, e carregados na proxima inicializacao. O arquivo usa texto no formato chave-valor e pode ser removido para iniciar um cadastro novo.

O arquivo contem dados pessoais, como CPF e e-mail, e nao e criptografado. Mantenha-o em um local protegido e nao o compartilhe.

## Regras principais

- o funcionário deve existir antes do cadastro de produto e da entrada;
- o cliente deve ter CPF válido e estar cadastrado antes da venda;
- uma saída não pode ser maior que a quantidade disponível;
- códigos não podem ser repetidos;
- preço, quantidade, estoque mínimo e alíquota passam por validação.

## Tributação

As alíquotas usadas pelo programa têm finalidade acadêmica e servem para demonstrar o cálculo sobre o estoque. Uma aplicação comercial precisaria consultar a legislação e os dados fiscais vigentes.

## Autores

Vinícius Salgado, Vinícius Barbosa e José Elias.
