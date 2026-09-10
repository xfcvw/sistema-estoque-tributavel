# Projeto Integrador — Sistema de Gerenciamento de Estoque Tributável

## Contexto

Pequenos comércios precisam controlar a quantidade de seus produtos e conhecer o valor financeiro mantido no estoque. Quando os itens possuem tributação, também é útil estimar o valor de tributos associado a cada produto. O sistema organiza essas informações pelo terminal, sem depender de interface gráfica ou banco de dados.

## Objetivos

1. Cadastrar produtos e impedir códigos repetidos.
2. Registrar entradas e saídas de forma segura.
3. Alertar quando um item alcança seu estoque mínimo.
4. Calcular valor do estoque, tributos estimados e total com tributos.
5. Impedir que erros de digitação deixem o estoque em situação inválida.

## Diferencial

Além do controle de quantidade, o projeto trabalha com `BigDecimal` para valores financeiros — evitando imprecisões de `double` — e possui testes automatizados de robustez. Eles tentam enviar dados indevidos e confirmam que o sistema bloqueia a ação sem alterar o estoque já cadastrado.

## Regras de negócio

| Regra | Comportamento do sistema |
| --- | --- |
| Código | 3 a 20 caracteres; somente letras, números e hífen; único no cadastro. |
| Preço | Positivo e com até duas casas decimais. |
| Alíquota | De 0% a 100%, com até duas casas decimais. |
| Quantidades | Nunca podem ser negativas; entrada e saída devem ser maiores que zero. |
| Saída | Não pode ultrapassar o estoque disponível. |
| Tributo estimado | `(preço × quantidade) × alíquota ÷ 100`. |

## Fluxo principal (pseudocódigo)

```text
INÍCIO
  enquanto opção for diferente de 0
    mostrar menu
    ler opção
    se opção = cadastrar
      ler dados do produto
      validar dados e código único
      cadastrar produto
    senão se opção = entrada ou saída
      localizar produto pelo código
      validar quantidade
      se saída for maior que estoque
        informar erro
      senão
        atualizar estoque
    senão se opção = relatório
      somar valores e tributos de todos os produtos
      exibir totais
    fim se
  fim enquanto
FIM
```

## Como demonstrar na apresentação

1. Cadastre um produto com preço `20,00`, quantidade `10` e alíquota `18`.
2. Mostre o relatório: valor de estoque `R$ 200,00`, tributo `R$ 36,00` e total `R$ 236,00`.
3. Tente retirar `11` itens: o sistema deve bloquear e informar estoque insuficiente.
4. Consulte o produto e mostre que a quantidade continua `10`.
5. Execute `TesteSistema` para apresentar os testes automáticos.
