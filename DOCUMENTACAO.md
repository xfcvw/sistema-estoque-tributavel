# Projeto Integrador — Sistema de Gerenciamento de Estoque Tributável

## Contexto

Pequenos comércios precisam controlar a quantidade de seus produtos e conhecer o valor financeiro mantido no estoque. Quando os itens possuem tributação, também é útil estimar a carga tributária conforme o regime escolhido pela empresa. O sistema organiza essas informações pelo terminal, sem depender de interface gráfica ou banco de dados.

## Objetivos

1. Cadastrar funcionários, clientes e produtos com códigos únicos.
2. Exigir um funcionário cadastrado como responsável pelo cadastro e pelas entradas de produtos.
3. Registrar saídas/vendas somente para clientes cadastrados.
4. Alertar quando um item alcança seu estoque mínimo.
5. Escolher ou alterar o regime tributário: Simples Nacional, Lucro Presumido ou Lucro Real.
6. Calcular valor do estoque, tributos estimados e total com tributos conforme o regime selecionado.
7. Impedir que erros de digitação deixem o estoque em situação inválida.

## Diferencial

Além do controle de quantidade, o produto fica associado ao funcionário que o cadastrou. Entradas também exigem identificação do funcionário, enquanto saídas/vendas exigem um cliente válido. A mesma loja pode ser simulada nos três regimes tributários. A carga estimada muda imediatamente no relatório quando o regime é alterado. O projeto usa `BigDecimal` para valores financeiros — evitando imprecisões de `double` — e possui 62 testes automatizados de robustez.

## Regimes tributários da simulação

| Opção | Regime | Alíquota estimada usada no projeto |
| --- | --- | --- |
| 1 | Simples Nacional | 6,00% |
| 2 | Lucro Presumido | 11,33% |
| 3 | Lucro Real | 15,00% |

> As alíquotas são valores didáticos para comparar os cenários no projeto. O cálculo tributário oficial varia conforme atividade, faturamento, localidade e legislação vigente.

## Regras de negócio

| Regra | Comportamento do sistema |
| --- | --- |
| Código | 3 a 20 caracteres; somente letras, números e hífen; único no cadastro. |
| Código de funcionário | Formato `FUN-001` até `FUN-999999`; único. |
| Código de cliente | Formato `CLI-001` até `CLI-999999`; único. |
| Funcionário | Deve existir antes de cadastrar um produto ou registrar uma entrada. |
| Cliente | Deve existir antes de registrar uma saída/venda. |
| Opções do menu | Aceitam apenas um algarismo de `0` a `9`; valores como `10` ou texto são bloqueados. |
| Preço | Positivo e com até duas casas decimais. |
| Regime tributário | Deve ser Simples Nacional, Lucro Presumido ou Lucro Real. |
| Quantidades | Nunca podem ser negativas; entrada e saída devem ser maiores que zero. |
| Saída | Não pode ultrapassar o estoque disponível. |
| Tributo estimado | `(valor total do estoque × alíquota do regime) ÷ 100`. |

## Fluxo principal (pseudocódigo)

```text
INÍCIO
  enquanto opção for diferente de 0
    escolher regime tributário
    mostrar menu
    ler opção
    se opção = cadastrar funcionário ou cliente
      ler e validar os dados da pessoa
      impedir código repetido
      salvar cadastro
    senão se opção = cadastrar produto
      ler dados do produto
      validar funcionário responsável
      validar dados e código único
      cadastrar produto
    senão se opção = entrada
      validar funcionário responsável
      localizar produto pelo código
      validar quantidade
      atualizar estoque
    senão se opção = saída/venda
      validar cliente
      localizar produto pelo código
      validar quantidade
      se saída for maior que estoque
        informar erro
      senão
        atualizar estoque
    senão se opção = alterar regime
      ler e validar novo regime
      atualizar regime da loja
    senão se opção = relatório
      somar valores e aplicar a alíquota do regime selecionado
      exibir totais
    fim se
  fim enquanto
FIM
```

## Fluxograma

```mermaid
flowchart TD
    inicio([Início]) --> regime[Escolher regime tributário]
    regime --> menu[Mostrar menu e ler opção]
    menu --> opcao{Opção escolhida}
    opcao -->|Cadastrar pessoa| pessoa[Validar cliente ou funcionário]
    pessoa --> salvarPessoa[Salvar cadastro]
    opcao -->|Cadastrar produto| cadastro[Ler produto e validar funcionário]
    cadastro --> salvar[Cadastrar produto]
    opcao -->|Entrada| entrada[Validar funcionário e quantidade]
    entrada --> atualizar[Atualizar estoque]
    opcao -->|Venda| movimento[Validar cliente e produto]
    movimento --> suficiente{Saída cabe no estoque?}
    suficiente -->|Não| erro[Exibir mensagem de erro]
    suficiente -->|Sim| atualizar[Atualizar estoque]
    opcao -->|Alterar regime| trocarRegime[Selecionar novo regime]
    trocarRegime --> menu
    opcao -->|Relatório| relatorio[Aplicar alíquota do regime]
    salvar --> menu
    salvarPessoa --> menu
    atualizar --> menu
    erro --> menu
    relatorio --> menu
    opcao -->|Sair| fim([Fim])
```

O fluxo mostra que o sistema só atualiza o estoque depois de validar os dados.
Assim, uma saída maior que a quantidade disponível volta ao menu sem modificar o produto.

## Como demonstrar na apresentação

1. Escolha `Simples Nacional`, cadastre `FUN-001` e depois `CLI-001`.
2. Cadastre um produto com preço `20,00`, quantidade `10` e funcionário responsável `FUN-001`.
3. Registre uma venda de `3` unidades para `CLI-001` e mostre que o estoque passa para `7`.
4. Mostre o relatório: a tributação usa o regime escolhido e informa o total de clientes e funcionários.
5. Digite `10` como opção do menu: o sistema deve bloquear porque aceita somente um dígito.
6. Tente retirar uma quantidade maior que o estoque: o sistema deve bloquear e manter o estoque correto.
7. Execute `TesteSistema` para apresentar os 62 testes automáticos.
