programa
{
    inclua biblioteca Texto --> texto

    const inteiro LIMITE = 100

    // Vetores paralelos usados para guardar os dados dos clientes.
    cadeia cpfClientes[LIMITE]
    cadeia nomeClientes[LIMITE]
    cadeia emailClientes[LIMITE]
    inteiro totalClientes = 0

    // Vetores paralelos usados para guardar os dados dos funcionários.
    cadeia codigoFuncionarios[LIMITE]
    cadeia nomeFuncionarios[LIMITE]
    cadeia cargoFuncionarios[LIMITE]
    inteiro totalFuncionarios = 0

    // Vetores paralelos usados para guardar os dados dos produtos.
    cadeia codigoProdutos[LIMITE]
    cadeia nomeProdutos[LIMITE]
    cadeia categoriaProdutos[LIMITE]
    real precoProdutos[LIMITE]
    real aliquotaProdutos[LIMITE]
    inteiro quantidadeProdutos[LIMITE]
    inteiro minimoProdutos[LIMITE]
    cadeia responsavelProdutos[LIMITE]
    inteiro totalProdutos = 0

    // 1 = Simples Nacional, 2 = Lucro Presumido e 3 = Lucro Real.
    inteiro regime = 1

    // Inicia o sistema e mantém o menu principal aberto até o usuário escolher sair.
    funcao inicio()
    {
        cadeia opcao = ""

        escreva("=== Sistema de Gerenciamento de Estoque Tributável ===\n")
        regime = lerRegime()

        faca
        {
            mostrarMenu()
            opcao = lerOpcao("Opção: ")

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
                escreva("Opção inválida. Escolha um número do menu.\n")
            }
        }
        enquanto (opcao != "0")

        escreva("Sistema encerrado.\n")
    }

    // Mostra todas as ações disponíveis no sistema.
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

    // Cadastra um funcionário que poderá registrar produtos e entradas.
    funcao cadastrarFuncionario()
    {
        cadeia codigo, nome, cargo

        se (totalFuncionarios >= LIMITE)
        {
            escreva("Limite de funcionários atingido.\n")
            retorne
        }

        codigo = lerTextoObrigatorio("Código do funcionário (FUN-001): ")
        se (nao codigoFuncionarioValido(codigo))
        {
            escreva("Use o formato FUN-001 até FUN-999999.\n")
            retorne
        }
        se (buscarFuncionario(codigo) != -1)
        {
            escreva("Funcionário já cadastrado.\n")
            retorne
        }

        nome = lerTextoObrigatorio("Nome: ")
        cargo = lerTextoObrigatorio("Cargo: ")

        codigoFuncionarios[totalFuncionarios] = codigo
        nomeFuncionarios[totalFuncionarios] = nome
        cargoFuncionarios[totalFuncionarios] = cargo
        totalFuncionarios++
        escreva("Funcionário cadastrado com sucesso.\n")
    }

    // Cadastra um cliente usando CPF válido e sem permitir duplicidade.
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

        nome = lerTextoObrigatorio("Nome: ")
        email = lerEmail()

        cpfClientes[totalClientes] = cpf
        nomeClientes[totalClientes] = nome
        emailClientes[totalClientes] = email
        totalClientes++
        escreva("Cliente cadastrado com sucesso.\n")
    }

    // Cadastra o produto, seus dados de estoque e sua alíquota tributária.
    funcao cadastrarProduto()
    {
        cadeia codigo, nome, categoria, responsavel
        real preco, aliquotaProduto
        inteiro quantidade, minimo

        se (totalProdutos >= LIMITE)
        {
            escreva("Limite de produtos atingido.\n")
            retorne
        }

        codigo = lerTextoObrigatorio("Código do produto: ")
        se (nao codigoProdutoValido(codigo))
        {
            escreva("Código deve ter de 3 a 20 caracteres, sem espaços.\n")
            retorne
        }
        se (buscarProduto(codigo) != -1)
        {
            escreva("Produto já cadastrado.\n")
            retorne
        }

        responsavel = lerTextoObrigatorio("Código do funcionário responsável: ")
        se (buscarFuncionario(responsavel) == -1)
        {
            escreva("Funcionário não encontrado.\n")
            retorne
        }

        nome = lerTextoObrigatorio("Nome: ")
        categoria = lerTextoObrigatorio("Categoria: ")
        preco = lerPreco()
        quantidade = lerInteiroNaoNegativo("Quantidade inicial: ")
        minimo = lerInteiroNaoNegativo("Estoque mínimo: ")
        aliquotaProduto = lerAliquota()

        codigoProdutos[totalProdutos] = codigo
        nomeProdutos[totalProdutos] = nome
        categoriaProdutos[totalProdutos] = categoria
        precoProdutos[totalProdutos] = preco
        aliquotaProdutos[totalProdutos] = aliquotaProduto
        quantidadeProdutos[totalProdutos] = quantidade
        minimoProdutos[totalProdutos] = minimo
        responsavelProdutos[totalProdutos] = responsavel
        totalProdutos++
        escreva("Produto cadastrado com sucesso.\n")
    }

    // Soma uma quantidade recebida ao estoque de um produto.
    funcao registrarEntrada()
    {
        cadeia codigo, funcionario
        inteiro produto, quantidade

        codigo = lerTextoObrigatorio("Código do produto: ")
        produto = buscarProduto(codigo)
        se (produto == -1)
        {
            escreva("Produto não encontrado.\n")
            retorne
        }

        quantidade = lerInteiroPositivo("Quantidade de entrada: ")
        funcionario = lerTextoObrigatorio("Código do funcionário: ")
        se (buscarFuncionario(funcionario) == -1)
        {
            escreva("Funcionário não encontrado.\n")
            retorne
        }

        quantidadeProdutos[produto] = quantidadeProdutos[produto] + quantidade
        escreva("Entrada registrada com sucesso.\n")
    }

    // Registra uma venda e reduz o estoque somente quando há quantidade disponível.
    funcao registrarSaida()
    {
        cadeia codigo, cpf
        inteiro produto, quantidade

        codigo = lerTextoObrigatorio("Código do produto: ")
        produto = buscarProduto(codigo)
        se (produto == -1)
        {
            escreva("Produto não encontrado.\n")
            retorne
        }

        quantidade = lerInteiroPositivo("Quantidade de saída: ")
        cpf = lerCpf()

        se (buscarCliente(cpf) == -1)
        {
            escreva("Cliente não encontrado.\n")
            retorne
        }
        se (quantidade > quantidadeProdutos[produto])
        {
            escreva("Estoque insuficiente.\n")
            retorne
        }

        quantidadeProdutos[produto] = quantidadeProdutos[produto] - quantidade
        escreva("Saída/venda registrada com sucesso.\n")
    }

    // Lista todos os funcionários já cadastrados.
    funcao listarFuncionarios()
    {
        inteiro i

        se (totalFuncionarios == 0)
        {
            escreva("Nenhum funcionário cadastrado.\n")
            retorne
        }

        escreva("\n--- Funcionários cadastrados ---\n")
        para (i = 0; i < totalFuncionarios; i++)
        {
            escreva("[", codigoFuncionarios[i], "] ", nomeFuncionarios[i],
                    " | Cargo: ", cargoFuncionarios[i], "\n")
        }
    }

    // Lista todos os clientes já cadastrados.
    funcao listarClientes()
    {
        inteiro i

        se (totalClientes == 0)
        {
            escreva("Nenhum cliente cadastrado.\n")
            retorne
        }

        escreva("\n--- Clientes cadastrados ---\n")
        para (i = 0; i < totalClientes; i++)
        {
            escreva("[CPF: ", cpfClientes[i], "] ", nomeClientes[i],
                    " | E-mail: ", emailClientes[i], "\n")
        }
    }

    // Exibe o submenu de consultas de estoque.
    funcao menuEstoque()
    {
        cadeia opcao = ""

        faca
        {
            escreva("\n--- Consulta de estoque ---\n")
            escreva("1 - Listar todos os produtos\n")
            escreva("2 - Listar produtos no estoque mínimo\n")
            escreva("0 - Voltar\n")

            opcao = lerOpcao("Opção: ")

            se (opcao == "1")
            {
                listarProdutos(falso)
            }
            senao se (opcao == "2")
            {
                listarProdutos(verdadeiro)
            }
            senao se (opcao != "0")
            {
                escreva("Opção inválida.\n")
            }
        }
        enquanto (opcao != "0")
    }

    // Mostra produtos e, quando solicitado, filtra os que estão no estoque mínimo.
    funcao listarProdutos(logico somenteMinimo)
    {
        inteiro i
        logico encontrou = falso

        para (i = 0; i < totalProdutos; i++)
        {
            se (nao somenteMinimo ou quantidadeProdutos[i] <= minimoProdutos[i])
            {
                se (nao encontrou)
                {
                    se (somenteMinimo)
                    {
                        escreva("\n--- Produtos no estoque mínimo ---\n")
                    }
                    senao
                    {
                        escreva("\n--- Produtos em estoque ---\n")
                    }
                }

                exibirProduto(i)
                encontrou = verdadeiro
            }
        }

        se (nao encontrou)
        {
            se (somenteMinimo)
            {
                escreva("Nenhum produto está no estoque mínimo.\n")
            }
            senao
            {
                escreva("Nenhum produto cadastrado.\n")
            }
        }
    }

    // Mostra todos os dados importantes de um produto encontrado.
    funcao exibirProduto(inteiro indice)
    {
        real valorEstoque, tributoEstimado

        valorEstoque = precoProdutos[indice] * quantidadeProdutos[indice]
        tributoEstimado = valorEstoque * aliquotaProdutos[indice] / 100.0

        escreva("\n[", codigoProdutos[indice], "] ", nomeProdutos[indice], "\n")
        escreva("Categoria: ", categoriaProdutos[indice], "\n")
        escreva("Estoque: ", quantidadeProdutos[indice],
                " (mínimo: ", minimoProdutos[indice], ")\n")
        escreva("Cadastrado por: ", responsavelProdutos[indice], "\n")
        escreva("Preço: R$ ", precoProdutos[indice], "\n")
        escreva("Alíquota cadastrada: ", aliquotaProdutos[indice], "%\n")
        escreva("Tributo estimado no estoque: R$ ", tributoEstimado, "\n")
    }

    // Exibe o submenu de relatórios e alteração do regime tributário.
    funcao menuRelatorios()
    {
        cadeia opcao = ""

        faca
        {
            escreva("\n--- Relatórios e regime tributário ---\n")
            escreva("1 - Relatório tributável\n")
            escreva("2 - Alterar regime tributário\n")
            escreva("0 - Voltar\n")

            opcao = lerOpcao("Opção: ")

            se (opcao == "1")
            {
                mostrarRelatorio()
            }
            senao se (opcao == "2")
            {
                regime = lerRegime()
            }
            senao se (opcao != "0")
            {
                escreva("Opção inválida.\n")
            }
        }
        enquanto (opcao != "0")
    }

    // Calcula o valor dos produtos, os tributos estimados e os itens no mínimo.
    funcao mostrarRelatorio()
    {
        inteiro i, noMinimo = 0
        real valor = 0.0, tributo = 0.0

        para (i = 0; i < totalProdutos; i++)
        {
            valor = valor + precoProdutos[i] * quantidadeProdutos[i]
            tributo = tributo + precoProdutos[i] * quantidadeProdutos[i] *
                      aliquotaProdutos[i] / 100.0

            se (quantidadeProdutos[i] <= minimoProdutos[i])
            {
                noMinimo++
            }
        }

        escreva("\n--- Relatório de Estoque Tributável ---\n")
        escreva("Regime: ", nomeRegime(), "\n")
        escreva("Alíquotas: calculadas individualmente por produto.\n")
        escreva("Valor dos produtos: R$ ", valor, "\n")
        escreva("Tributos estimados: R$ ", tributo, "\n")
        escreva("Valor total com tributos: R$ ", valor + tributo, "\n")
        escreva("Itens no mínimo/abaixo: ", noMinimo, "\n")
    }

    // Lê o regime tributário e limita a escolha às três opções disponíveis.
    funcao inteiro lerRegime()
    {
        cadeia opcao

        faca
        {
            escreva("\n1 - Simples Nacional | 2 - Lucro Presumido | 3 - Lucro Real\n")
            opcao = lerOpcao("Escolha o regime: ")

            se (opcao != "1" e opcao != "2" e opcao != "3")
            {
                escreva("Escolha 1, 2 ou 3.\n")
            }
        }
        enquanto (opcao != "1" e opcao != "2" e opcao != "3")

        se (opcao == "1") retorne 1
        se (opcao == "2") retorne 2
        retorne 3
    }

    // Converte o número do regime em seu nome para uso nos relatórios.
    funcao cadeia nomeRegime()
    {
        se (regime == 1) retorne "Simples Nacional"
        se (regime == 2) retorne "Lucro Presumido"
        retorne "Lucro Real"
    }

    // Lê uma opção com apenas um algarismo, evitando letras e valores longos.
    funcao cadeia lerOpcao(cadeia rotulo)
    {
        cadeia opcao

        faca
        {
            escreva(rotulo)
            leia(opcao)

            se (nao somenteDigitos(opcao, 1, 1))
            {
                escreva("Digite somente um algarismo de 0 a 9.\n")
            }
        }
        enquanto (nao somenteDigitos(opcao, 1, 1))

        retorne opcao
    }

    // Lê texto obrigatório com tamanho entre 2 e 80 caracteres.
    funcao cadeia lerTextoObrigatorio(cadeia rotulo)
    {
        cadeia valor

        faca
        {
            escreva(rotulo)
            leia(valor)

            se (texto.numero_caracteres(valor) < 2 ou texto.numero_caracteres(valor) > 80)
            {
                escreva("O texto deve ter entre 2 e 80 caracteres.\n")
            }
        }
        enquanto (texto.numero_caracteres(valor) < 2 ou texto.numero_caracteres(valor) > 80)

        retorne valor
    }

    // Lê um inteiro sem sinal e com no máximo nove algarismos.
    funcao inteiro lerInteiroNaoNegativo(cadeia rotulo)
    {
        cadeia valor

        faca
        {
            escreva(rotulo)
            leia(valor)

            se (nao somenteDigitos(valor, 1, 9))
            {
                escreva("Digite um número inteiro não negativo com até 9 algarismos.\n")
            }
        }
        enquanto (nao somenteDigitos(valor, 1, 9))

        retorne cadeia_para_inteiro(valor, 10)
    }

    // Lê uma quantidade inteira que obrigatoriamente deve ser maior que zero.
    funcao inteiro lerInteiroPositivo(cadeia rotulo)
    {
        inteiro valor

        faca
        {
            valor = lerInteiroNaoNegativo(rotulo)

            se (valor <= 0)
            {
                escreva("O valor deve ser maior que zero.\n")
            }
        }
        enquanto (valor <= 0)

        retorne valor
    }

    // Lê o preço e permite o uso de vírgula ou ponto como separador decimal.
    funcao real lerPreco()
    {
        cadeia valor
        real preco

        faca
        {
            escreva("Preço unitário (ex.: 19,90): ")
            leia(valor)

            se (nao numeroDecimalValido(valor))
            {
                escreva("Digite um preço válido.\n")
            }
            senao
            {
                valor = texto.substituir(valor, ",", ".")
                preco = cadeia_para_real(valor)

                se (preco <= 0)
                {
                    escreva("O preço deve ser maior que zero.\n")
                }
            }
        }
        enquanto (nao numeroDecimalValido(valor) ou preco <= 0)

        retorne preco
    }

    // Lê uma alíquota percentual entre 0 e 100.
    funcao real lerAliquota()
    {
        cadeia valor
        real aliquota

        faca
        {
            escreva("Alíquota tributária do produto (%): ")
            leia(valor)

            se (nao numeroDecimalValido(valor))
            {
                escreva("Digite uma alíquota válida.\n")
            }
            senao
            {
                valor = texto.substituir(valor, ",", ".")
                aliquota = cadeia_para_real(valor)

                se (aliquota < 0 ou aliquota > 100)
                {
                    escreva("A alíquota deve ficar entre 0 e 100.\n")
                }
            }
        }
        enquanto (nao numeroDecimalValido(valor) ou aliquota < 0 ou aliquota > 100)

        retorne aliquota
    }

    // Lê CPF de 11 dígitos e confirma os dois dígitos verificadores.
    funcao cadeia lerCpf()
    {
        cadeia cpf

        faca
        {
            escreva("CPF do cliente (11 números, sem letras ou símbolos): ")
            leia(cpf)

            se (nao cpfValido(cpf))
            {
                escreva("CPF inválido. Digite um CPF válido com 11 números.\n")
            }
        }
        enquanto (nao cpfValido(cpf))

        retorne cpf
    }

    // Faz uma verificação simples do formato do e-mail.
    funcao cadeia lerEmail()
    {
        cadeia email

        faca
        {
            escreva("E-mail: ")
            leia(email)

            se (nao emailValido(email))
            {
                escreva("E-mail inválido. Use o formato nome@dominio.com.\n")
            }
        }
        enquanto (nao emailValido(email))

        retorne email
    }

    // Confere se uma cadeia contém somente números e respeita o tamanho informado.
    funcao logico somenteDigitos(cadeia valor, inteiro tamanhoMinimo, inteiro tamanhoMaximo)
    {
        inteiro i
        cadeia digito

        se (texto.numero_caracteres(valor) < tamanhoMinimo ou
            texto.numero_caracteres(valor) > tamanhoMaximo)
        {
            retorne falso
        }

        para (i = 0; i < texto.numero_caracteres(valor); i++)
        {
            digito = texto.extrair_subtexto(valor, i, i)

            se (digito != "0" e digito != "1" e digito != "2" e digito != "3" e
                digito != "4" e digito != "5" e digito != "6" e digito != "7" e
                digito != "8" e digito != "9")
            {
                retorne falso
            }
        }

        retorne verdadeiro
    }

    // Confere se o texto representa um número decimal com no máximo um separador.
    funcao logico numeroDecimalValido(cadeia valor)
    {
        inteiro i, separadores = 0
        cadeia caractere

        se (texto.numero_caracteres(valor) < 1 ou texto.numero_caracteres(valor) > 12)
        {
            retorne falso
        }

        para (i = 0; i < texto.numero_caracteres(valor); i++)
        {
            caractere = texto.extrair_subtexto(valor, i, i)

            se (caractere == "," ou caractere == ".")
            {
                separadores++
            }
            senao se (nao somenteDigitos(caractere, 1, 1))
            {
                retorne falso
            }
        }

        se (separadores > 1)
        {
            retorne falso
        }

        retorne verdadeiro
    }

    // Valida o padrão FUN-001 até FUN-999999.
    funcao logico codigoFuncionarioValido(cadeia codigo)
    {
        inteiro tamanho

        tamanho = texto.numero_caracteres(codigo)

        se (tamanho < 7 ou tamanho > 10)
        {
            retorne falso
        }
        se (texto.extrair_subtexto(codigo, 0, 3) != "FUN-")
        {
            retorne falso
        }

        retorne somenteDigitos(texto.extrair_subtexto(codigo, 4, tamanho - 1), 3, 6)
    }

    // Valida o tamanho do código do produto e impede espaços.
    funcao logico codigoProdutoValido(cadeia codigo)
    {
        inteiro i, tamanho
        cadeia caractere

        tamanho = texto.numero_caracteres(codigo)

        se (tamanho < 3 ou tamanho > 20)
        {
            retorne falso
        }

        para (i = 0; i < tamanho; i++)
        {
            caractere = texto.extrair_subtexto(codigo, i, i)

            se (caractere == " ")
            {
                retorne falso
            }
        }

        retorne verdadeiro
    }

    // Verifica o formato básico do e-mail sem permitir espaços.
    funcao logico emailValido(cadeia email)
    {
        inteiro i, posicaoArroba = -1, posicaoPonto = -1, tamanho
        cadeia caractere

        tamanho = texto.numero_caracteres(email)

        se (tamanho < 6 ou tamanho > 80)
        {
            retorne falso
        }

        para (i = 0; i < tamanho; i++)
        {
            caractere = texto.extrair_subtexto(email, i, i)

            se (caractere == " ")
            {
                retorne falso
            }
            se (caractere == "@")
            {
                posicaoArroba = i
            }
            se (caractere == "." e i > posicaoArroba)
            {
                posicaoPonto = i
            }
        }

        retorne posicaoArroba > 0 e posicaoPonto > posicaoArroba + 1 e
                posicaoPonto < tamanho - 1
    }

    // Calcula os dois dígitos verificadores e valida se o CPF é verdadeiro.
    funcao logico cpfValido(cadeia cpf)
    {
        inteiro i, soma, primeiro, segundo
        cadeia primeiroDigito, digito

        se (nao somenteDigitos(cpf, 11, 11))
        {
            retorne falso
        }

        primeiroDigito = texto.extrair_subtexto(cpf, 0, 0)

        // Impede CPFs repetidos, como 11111111111.
        para (i = 1; i < 11; i++)
        {
            se (texto.extrair_subtexto(cpf, i, i) != primeiroDigito)
            {
                pare
            }

            se (i == 10)
            {
                retorne falso
            }
        }

        soma = 0
        para (i = 0; i < 9; i++)
        {
            digito = texto.extrair_subtexto(cpf, i, i)
            soma = soma + digitoParaInteiro(digito) * (10 - i)
        }

        se (soma % 11 < 2)
        {
            primeiro = 0
        }
        senao
        {
            primeiro = 11 - (soma % 11)
        }

        soma = 0
        para (i = 0; i < 10; i++)
        {
            digito = texto.extrair_subtexto(cpf, i, i)
            soma = soma + digitoParaInteiro(digito) * (11 - i)
        }

        se (soma % 11 < 2)
        {
            segundo = 0
        }
        senao
        {
            segundo = 11 - (soma % 11)
        }

        retorne digitoParaInteiro(texto.extrair_subtexto(cpf, 9, 9)) == primeiro e
                digitoParaInteiro(texto.extrair_subtexto(cpf, 10, 10)) == segundo
    }

    // Converte um único algarismo de texto em número inteiro.
    funcao inteiro digitoParaInteiro(cadeia digito)
    {
        se (digito == "0") retorne 0
        se (digito == "1") retorne 1
        se (digito == "2") retorne 2
        se (digito == "3") retorne 3
        se (digito == "4") retorne 4
        se (digito == "5") retorne 5
        se (digito == "6") retorne 6
        se (digito == "7") retorne 7
        se (digito == "8") retorne 8
        retorne 9
    }

    // Procura um cliente pelo CPF e devolve sua posição no vetor.
    funcao inteiro buscarCliente(cadeia cpf)
    {
        inteiro i

        para (i = 0; i < totalClientes; i++)
        {
            se (cpfClientes[i] == cpf)
            {
                retorne i
            }
        }

        retorne -1
    }

    // Procura um funcionário pelo código e devolve sua posição no vetor.
    funcao inteiro buscarFuncionario(cadeia codigo)
    {
        inteiro i

        para (i = 0; i < totalFuncionarios; i++)
        {
            se (codigoFuncionarios[i] == codigo)
            {
                retorne i
            }
        }

        retorne -1
    }

    // Procura um produto pelo código e devolve sua posição no vetor.
    funcao inteiro buscarProduto(cadeia codigo)
    {
        inteiro i

        para (i = 0; i < totalProdutos; i++)
        {
            se (codigoProdutos[i] == codigo)
            {
                retorne i
            }
        }

        retorne -1
    }
}
