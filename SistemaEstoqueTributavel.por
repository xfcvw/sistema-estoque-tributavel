programa
{
    inclua biblioteca Texto --> texto

    const inteiro LIMITE = 100

    // Vetores paralelos para os clientes.
    cadeia cpfClientes[LIMITE]
    cadeia nomeClientes[LIMITE]
    cadeia emailClientes[LIMITE]
    inteiro totalClientes = 0

    // Vetores paralelos para os funcionários.
    cadeia codigoFuncionarios[LIMITE]
    cadeia nomeFuncionarios[LIMITE]
    cadeia cargoFuncionarios[LIMITE]
    inteiro totalFuncionarios = 0

    // Vetores paralelos para os produtos.
    cadeia codigoProdutos[LIMITE]
    cadeia nomeProdutos[LIMITE]
    cadeia categoriaProdutos[LIMITE]
    real precoProdutos[LIMITE]
    inteiro quantidadeProdutos[LIMITE]
    inteiro minimoProdutos[LIMITE]
    cadeia responsavelProdutos[LIMITE]
    inteiro totalProdutos = 0

    inteiro regime = 1

    funcao inicio()
    {
        cadeia opcao = ""

        escreva("=== Sistema de Gerenciamento de Estoque Tributável ===\n")
        regime = lerRegime()

        faca
        {
            mostrarMenu()
            escreva("Opção: ")
            leia(opcao)

            se (opcao == "1")
            {
                cadastrarFuncionario()
            }
            senao se (opcao == "2")
            {
                cadastrarCliente()
            }
            senao se (opcao == "3")
            {
                listarFuncionarios()
            }
            senao se (opcao == "4")
            {
                listarClientes()
            }
            senao se (opcao == "5")
            {
                cadastrarProduto()
            }
            senao se (opcao == "6")
            {
                registrarEntrada()
            }
            senao se (opcao == "7")
            {
                registrarSaida()
            }
            senao se (opcao == "8")
            {
                menuEstoque()
            }
            senao se (opcao == "9")
            {
                menuRelatorios()
            }
            senao se (opcao != "0")
            {
                escreva("Opção inválida. Digite somente um número do menu.\n")
            }
        }
        enquanto (opcao != "0")

        escreva("Sistema encerrado.\n")
    }

    funcao mostrarMenu()
    {
        escreva("\n1 - Cadastrar funcionário\n")
        escreva("2 - Cadastrar cliente\n")
        escreva("3 - Listar funcionários\n")
        escreva("4 - Listar clientes\n")
        escreva("5 - Cadastrar produto\n")
        escreva("6 - Registrar entrada\n")
        escreva("7 - Registrar saída/venda\n")
        escreva("8 - Consultar estoque\n")
        escreva("9 - Relatórios e regime tributário\n")
        escreva("0 - Sair\n")
    }

    funcao cadastrarFuncionario()
    {
        cadeia codigo, nome, cargo

        se (totalFuncionarios >= LIMITE)
        {
            escreva("Limite de funcionários atingido.\n")
            retorne
        }

        escreva("Código do funcionário (FUN-001): ")
        leia(codigo)
        se (buscarFuncionario(codigo) != -1)
        {
            escreva("Funcionário já cadastrado.\n")
            retorne
        }

        escreva("Nome: ")
        leia(nome)
        escreva("Cargo: ")
        leia(cargo)

        codigoFuncionarios[totalFuncionarios] = codigo
        nomeFuncionarios[totalFuncionarios] = nome
        cargoFuncionarios[totalFuncionarios] = cargo
        totalFuncionarios++
        escreva("Funcionário cadastrado com sucesso.\n")
    }

    funcao cadastrarCliente()
    {
        cadeia cpf, nome, email

        se (totalClientes >= LIMITE)
        {
            escreva("Limite de clientes atingido.\n")
            retorne
        }

        cpf = lerCpf()
        se (buscarCliente(cpf) != -1)
        {
            escreva("Cliente já cadastrado.\n")
            retorne
        }

        escreva("Nome: ")
        leia(nome)
        escreva("E-mail: ")
        leia(email)

        cpfClientes[totalClientes] = cpf
        nomeClientes[totalClientes] = nome
        emailClientes[totalClientes] = email
        totalClientes++
        escreva("Cliente cadastrado com sucesso.\n")
    }

    funcao cadastrarProduto()
    {
        cadeia codigo, nome, categoria, responsavel
        real preco
        inteiro quantidade, minimo

        se (totalProdutos >= LIMITE)
        {
            escreva("Limite de produtos atingido.\n")
            retorne
        }

        escreva("Código do produto: ")
        leia(codigo)
        se (buscarProduto(codigo) != -1)
        {
            escreva("Produto já cadastrado.\n")
            retorne
        }

        escreva("Código do funcionário responsável: ")
        leia(responsavel)
        se (buscarFuncionario(responsavel) == -1)
        {
            escreva("Funcionário não encontrado.\n")
            retorne
        }

        escreva("Nome: ")
        leia(nome)
        escreva("Categoria: ")
        leia(categoria)
        escreva("Preço unitário: ")
        leia(preco)
        escreva("Quantidade inicial: ")
        leia(quantidade)
        escreva("Estoque mínimo: ")
        leia(minimo)

        se (preco <= 0 ou quantidade < 0 ou minimo < 0)
        {
            escreva("Preço e quantidades inválidos.\n")
            retorne
        }

        codigoProdutos[totalProdutos] = codigo
        nomeProdutos[totalProdutos] = nome
        categoriaProdutos[totalProdutos] = categoria
        precoProdutos[totalProdutos] = preco
        quantidadeProdutos[totalProdutos] = quantidade
        minimoProdutos[totalProdutos] = minimo
        responsavelProdutos[totalProdutos] = responsavel
        totalProdutos++
        escreva("Produto cadastrado com sucesso.\n")
    }

    funcao registrarEntrada()
    {
        cadeia codigo, funcionario
        inteiro produto, quantidade

        escreva("Código do produto: ")
        leia(codigo)
        produto = buscarProduto(codigo)
        se (produto == -1)
        {
            escreva("Produto não encontrado.\n")
            retorne
        }

        escreva("Quantidade de entrada: ")
        leia(quantidade)
        escreva("Código do funcionário: ")
        leia(funcionario)

        se (quantidade <= 0 ou buscarFuncionario(funcionario) == -1)
        {
            escreva("Quantidade ou funcionário inválido.\n")
            retorne
        }

        quantidadeProdutos[produto] = quantidadeProdutos[produto] + quantidade
        escreva("Entrada registrada com sucesso.\n")
    }

    funcao registrarSaida()
    {
        cadeia codigo, cpf
        inteiro produto, quantidade

        escreva("Código do produto: ")
        leia(codigo)
        produto = buscarProduto(codigo)
        se (produto == -1)
        {
            escreva("Produto não encontrado.\n")
            retorne
        }

        escreva("Quantidade de saída: ")
        leia(quantidade)
        cpf = lerCpf()

        se (buscarCliente(cpf) == -1)
        {
            escreva("Cliente não encontrado.\n")
            retorne
        }
        se (quantidade <= 0 ou quantidade > quantidadeProdutos[produto])
        {
            escreva("Quantidade inválida ou estoque insuficiente.\n")
            retorne
        }

        quantidadeProdutos[produto] = quantidadeProdutos[produto] - quantidade
        escreva("Saída/venda registrada com sucesso.\n")
    }

    funcao listarFuncionarios()
    {
        inteiro i
        se (totalFuncionarios == 0)
        {
            escreva("Nenhum funcionário cadastrado.\n")
            retorne
        }
        para (i = 0; i < totalFuncionarios; i++)
        {
            escreva("[", codigoFuncionarios[i], "] ", nomeFuncionarios[i], " | Cargo: ", cargoFuncionarios[i], "\n")
        }
    }

    funcao listarClientes()
    {
        inteiro i
        se (totalClientes == 0)
        {
            escreva("Nenhum cliente cadastrado.\n")
            retorne
        }
        para (i = 0; i < totalClientes; i++)
        {
            escreva("[CPF: ", cpfClientes[i], "] ", nomeClientes[i], " | E-mail: ", emailClientes[i], "\n")
        }
    }

    funcao menuEstoque()
    {
        cadeia opcao = ""
        faca
        {
            escreva("\n1 - Listar todos os produtos\n2 - Produtos no estoque mínimo\n0 - Voltar\nOpção: ")
            leia(opcao)
            se (opcao == "1") listarProdutos(falso)
            senao se (opcao == "2") listarProdutos(verdadeiro)
            senao se (opcao != "0") escreva("Opção inválida.\n")
        }
        enquanto (opcao != "0")
    }

    funcao listarProdutos(logico somenteMinimo)
    {
        inteiro i
        logico encontrou = falso
        para (i = 0; i < totalProdutos; i++)
        {
            se (nao somenteMinimo ou quantidadeProdutos[i] <= minimoProdutos[i])
            {
                escreva("[", codigoProdutos[i], "] ", nomeProdutos[i], " | Estoque: ", quantidadeProdutos[i], " | Mínimo: ", minimoProdutos[i], "\n")
                encontrou = verdadeiro
            }
        }
        se (nao encontrou) escreva("Nenhum produto encontrado.\n")
    }

    funcao menuRelatorios()
    {
        cadeia opcao = ""
        faca
        {
            escreva("\n1 - Relatório tributável\n2 - Alterar regime\n0 - Voltar\nOpção: ")
            leia(opcao)
            se (opcao == "1") mostrarRelatorio()
            senao se (opcao == "2") regime = lerRegime()
            senao se (opcao != "0") escreva("Opção inválida.\n")
        }
        enquanto (opcao != "0")
    }

    funcao mostrarRelatorio()
    {
        inteiro i, noMinimo = 0
        real valor = 0.0, tributo
        para (i = 0; i < totalProdutos; i++)
        {
            valor = valor + precoProdutos[i] * quantidadeProdutos[i]
            se (quantidadeProdutos[i] <= minimoProdutos[i]) noMinimo++
        }
        tributo = valor * aliquota() / 100.0
        escreva("\nRegime: ", nomeRegime(), "\n")
        escreva("Valor dos produtos: R$ ", valor, "\n")
        escreva("Tributos estimados: R$ ", tributo, "\n")
        escreva("Valor total: R$ ", valor + tributo, "\n")
        escreva("Itens no mínimo/abaixo: ", noMinimo, "\n")
    }

    funcao inteiro lerRegime()
    {
        inteiro opcao
        faca
        {
            escreva("\n1 - Simples Nacional | 2 - Lucro Presumido | 3 - Lucro Real\nEscolha: ")
            leia(opcao)
        }
        enquanto (opcao < 1 ou opcao > 3)
        retorne opcao
    }

    funcao cadeia lerCpf()
    {
        cadeia cpf
        faca
        {
            escreva("CPF do cliente (11 números): ")
            leia(cpf)
            se (nao cpfSomenteNumeros(cpf)) escreva("CPF inválido. Digite somente 11 números.\n")
        }
        enquanto (nao cpfSomenteNumeros(cpf))
        retorne cpf
    }

    // Confere tamanho e garante que cada caractere é um algarismo.
    funcao logico cpfSomenteNumeros(cadeia cpf)
    {
        inteiro i
        cadeia digito
        se (texto.numero_caracteres(cpf) != 11) retorne falso

        para (i = 0; i < 11; i++)
        {
            digito = texto.extrair_subtexto(cpf, i, i)
            se (digito != "0" e digito != "1" e digito != "2" e digito != "3" e digito != "4" e digito != "5" e digito != "6" e digito != "7" e digito != "8" e digito != "9") retorne falso
        }
        retorne verdadeiro
    }

    funcao inteiro buscarCliente(cadeia cpf)
    {
        inteiro i
        para (i = 0; i < totalClientes; i++) se (cpfClientes[i] == cpf) retorne i
        retorne -1
    }

    funcao inteiro buscarFuncionario(cadeia codigo)
    {
        inteiro i
        para (i = 0; i < totalFuncionarios; i++) se (codigoFuncionarios[i] == codigo) retorne i
        retorne -1
    }

    funcao inteiro buscarProduto(cadeia codigo)
    {
        inteiro i
        para (i = 0; i < totalProdutos; i++) se (codigoProdutos[i] == codigo) retorne i
        retorne -1
    }

    funcao cadeia nomeRegime()
    {
        se (regime == 1) retorne "Simples Nacional"
        se (regime == 2) retorne "Lucro Presumido"
        retorne "Lucro Real"
    }

    funcao real aliquota()
    {
        se (regime == 1) retorne 6.0
        se (regime == 2) retorne 11.33
        retorne 15.0
    }
}
