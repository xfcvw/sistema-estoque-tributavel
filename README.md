# Sistema de Gerenciamento de Estoque Tributável

Projeto Java para terminal, sem interface gráfica e sem dependências externas.

## Funções

- Cadastro de produto com código, categoria, preço, quantidade, estoque mínimo e alíquota tributária.
- Entrada e saída de estoque.
- Consulta e listagem de produtos.
- Alerta de produtos no estoque mínimo.
- Relatório do valor do estoque, tributos estimados e valor total com tributos.
- Validação de códigos duplicados, números inválidos, preços e alíquotas inválidos, produto inexistente e saída maior que o estoque.

## Executar

Com um **JDK 17 ou superior** instalado, no terminal dentro desta pasta:

```bash
javac -d out src/*.java
java -cp out Main
```

## Rodar os testes de robustez

```bash
javac -d out src/*.java
java -cp out TesteSistema
```

Os testes tentam quebrar regras importantes: código duplicado ou inválido, preço/alíquota fora do limite, estoque negativo, entrada nula, produto inexistente e retirada acima do estoque. Eles também verificam que uma operação rejeitada não altera a quantidade existente.

> Observação: a alíquota usada é uma estimativa parametrizada no cadastro, adequada ao objetivo acadêmico. Em uso comercial, a regra tributária deve ser definida com orientação contábil/fiscal.
