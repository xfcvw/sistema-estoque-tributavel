# Sistema de Gerenciamento de Estoque Tributável

Aplicação Java para terminal que cadastra funcionários, clientes e produtos; controla entradas, vendas, estoque mínimo e uma simulação de tributação por regime: Simples Nacional, Lucro Presumido ou Lucro Real.

> As alíquotas exibidas são estimativas didáticas para o Projeto Integrador; não substituem uma apuração fiscal oficial.

Projeto Java para terminal, sem interface gráfica e sem dependências externas.

## Funções

- Escolha e alteração do regime: Simples Nacional, Lucro Presumido ou Lucro Real.
- Cadastro de funcionários (`FUN-001`) e clientes (`CLI-001`) com códigos únicos.
- Cadastro de produto com código, categoria, preço, quantidade e estoque mínimo.
- Produto e entrada vinculados a um funcionário cadastrado.
- Saída/venda vinculada a um cliente cadastrado.
- Alerta de produtos no estoque mínimo.
- Relatório do valor do estoque, tributos estimados e valor total com tributos conforme o regime escolhido.
- Opções do menu limitadas a um único algarismo (`0` a `9`).
- Validação de códigos duplicados, números inválidos, preços inválidos, regime inválido, pessoas inexistentes, produto inexistente e saída maior que o estoque.

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

Os 62 testes tentam quebrar regras importantes: códigos duplicados ou inválidos, campos nulos, e-mail/cargo inválidos, preço fora do limite, estoque negativo, entrada nula, funcionário ou cliente inexistente, regime inexistente, produto inexistente, retirada acima do estoque e proteção das listas internas. Eles também verificam que uma operação rejeitada não altera a quantidade existente.

> Observação: as alíquotas usadas são estimativas definidas para cada regime, adequadas ao objetivo acadêmico. Em uso comercial, a regra tributária deve ser definida com orientação contábil/fiscal.
