# Sistema de Gerenciamento de Estoque Tributável

Aplicação Java para terminal que controla produtos, entradas, saídas, estoque mínimo e uma simulação de tributação por regime: Simples Nacional, Lucro Presumido ou Lucro Real.

> As alíquotas exibidas são estimativas didáticas para o Projeto Integrador; não substituem uma apuração fiscal oficial.

Projeto Java para terminal, sem interface gráfica e sem dependências externas.

## Funções

- Escolha e alteração do regime: Simples Nacional, Lucro Presumido ou Lucro Real.
- Cadastro de produto com código, categoria, preço, quantidade e estoque mínimo.
- Entrada e saída de estoque.
- Consulta e listagem de produtos.
- Alerta de produtos no estoque mínimo.
- Relatório do valor do estoque, tributos estimados e valor total com tributos conforme o regime escolhido.
- Validação de códigos duplicados, números inválidos, preços inválidos, regime inválido, produto inexistente e saída maior que o estoque.

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

Os testes tentam quebrar regras importantes: código duplicado ou inválido, preço fora do limite, estoque negativo, entrada nula, regime inexistente, produto inexistente e retirada acima do estoque. Eles também verificam que uma operação rejeitada não altera a quantidade existente.

> Observação: as alíquotas usadas são estimativas definidas para cada regime, adequadas ao objetivo acadêmico. Em uso comercial, a regra tributária deve ser definida com orientação contábil/fiscal.
