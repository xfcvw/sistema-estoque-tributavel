# Sistema de Gerenciamento de Estoque Tributável

Aplicação Java procedural para terminal que cadastra funcionários, clientes e produtos; controla entradas, vendas, estoque mínimo e uma simulação de tributação por regime: Simples Nacional, Lucro Presumido ou Lucro Real.

> As alíquotas exibidas são estimativas didáticas para o Projeto Integrador; não substituem uma apuração fiscal oficial.

Projeto Java para terminal, sem interface gráfica e sem dependências externas. O código usa vetores, variáveis e métodos `static`, sem POO.

## Funções

- Escolha e alteração do regime: Simples Nacional, Lucro Presumido ou Lucro Real.
- Cadastro e listagem de funcionários (`FUN-001`) e clientes por CPF válido.
- Cadastro de produto com código, categoria, preço, quantidade e estoque mínimo.
- Produto e entrada vinculados a um funcionário cadastrado.
- Saída/venda vinculada a um cliente cadastrado.
- Consulta de estoque em submenu: lista todos os produtos ou somente os itens no estoque mínimo.
- Cadastro de alíquota tributária individual por produto e relatório com o total estimado de tributos.
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
java -cp out Main --teste
```

Os testes integrados tentam quebrar regras importantes: CPF com letras ou símbolos, CPF inválido, opção com mais de um algarismo, saída maior que o estoque e produto no estoque mínimo.

> Observação: a alíquota de cada produto é uma estimativa cadastrada para fins acadêmicos. Em uso comercial, ela deve ser definida a partir do NCM, da UF, da operação e de orientação contábil/fiscal.
