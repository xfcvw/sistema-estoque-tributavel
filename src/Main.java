import java.util.Scanner;

// Sistema de estoque feito com vetores e funções simples.
public class Main {
    static Scanner entrada = new Scanner(System.in);
    static final int LIMITE = 100;

    // Dados dos clientes.
    static String[] cpfClientes = new String[LIMITE];
    static String[] nomeClientes = new String[LIMITE];
    static String[] emailClientes = new String[LIMITE];
    static int totalClientes = 0;

    // Dados dos funcionários.
    static String[] codigoFuncionarios = new String[LIMITE];
    static String[] nomeFuncionarios = new String[LIMITE];
    static String[] cargoFuncionarios = new String[LIMITE];
    static int totalFuncionarios = 0;

    // Dados dos produtos.
    static String[] codigoProdutos = new String[LIMITE];
    static String[] nomeProdutos = new String[LIMITE];
    static String[] categoriaProdutos = new String[LIMITE];
    static double[] precoProdutos = new double[LIMITE];
    static double[] aliquotaProdutos = new double[LIMITE];
    static int[] quantidadeProdutos = new int[LIMITE];
    static int[] minimoProdutos = new int[LIMITE];
    static String[] responsavelProdutos = new String[LIMITE];
    static int totalProdutos = 0;

    // Guarda o regime tributário selecionado no início do sistema.
    static int regime = 1;

    public static void main(String[] args) {
        int opcao;

        System.out.println("=== Sistema de Gerenciamento de Estoque Tributável ===");
        regime = lerRegime();

        do {
            mostrarMenu();
            opcao = lerOpcao("Opção: ");

            if (opcao == 1) {
                cadastrarFuncionario();
            } else if (opcao == 2) {
                cadastrarCliente();
            } else if (opcao == 3) {
                listarFuncionarios();
            } else if (opcao == 4) {
                listarClientes();
            } else if (opcao == 5) {
                cadastrarProduto();
            } else if (opcao == 6) {
                registrarEntrada();
            } else if (opcao == 7) {
                registrarSaida();
            } else if (opcao == 8) {
                menuEstoque();
            } else if (opcao == 9) {
                menuRelatorios();
            } else if (opcao != 0) {
                System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        System.out.println("Sistema encerrado.");
    }

    // Mostra o menu principal.
    static void mostrarMenu() {
        System.out.println("\n1 - Cadastrar funcionário");
        System.out.println("2 - Cadastrar cliente");
        System.out.println("3 - Listar funcionários");
        System.out.println("4 - Listar clientes");
        System.out.println("5 - Cadastrar produto");
        System.out.println("6 - Registrar entrada");
        System.out.println("7 - Registrar saída/venda");
        System.out.println("8 - Consultar estoque");
        System.out.println("9 - Relatórios e regime tributário");
        System.out.println("0 - Sair");
    }

    // Cadastra um funcionário para registrar produtos e entradas.
    static void cadastrarFuncionario() {
        String codigo;

        if (totalFuncionarios == LIMITE) {
            System.out.println("Limite de funcionários atingido.");
            return;
        }

        codigo = lerCodigoFuncionario();

        if (buscarFuncionario(codigo) != -1) {
            System.out.println("Funcionário já cadastrado.");
            return;
        }

        codigoFuncionarios[totalFuncionarios] = codigo;
        nomeFuncionarios[totalFuncionarios] = lerTextoObrigatorio("Nome: ");
        cargoFuncionarios[totalFuncionarios] = lerTextoObrigatorio("Cargo: ");
        totalFuncionarios++;

        System.out.println("Funcionário cadastrado com sucesso.");
    }

    // Cadastra um cliente usando um CPF válido.
    static void cadastrarCliente() {
        String cpf;

        if (totalClientes == LIMITE) {
            System.out.println("Limite de clientes atingido.");
            return;
        }

        cpf = lerCpf();

        if (buscarCliente(cpf) != -1) {
            System.out.println("Cliente já cadastrado.");
            return;
        }

        cpfClientes[totalClientes] = cpf;
        nomeClientes[totalClientes] = lerTextoObrigatorio("Nome: ");
        emailClientes[totalClientes] = lerEmail();
        totalClientes++;

        System.out.println("Cliente cadastrado com sucesso.");
    }

    // Cadastra um produto e guarda suas informações de estoque e imposto.
    static void cadastrarProduto() {
        String codigo;
        String responsavel;

        if (totalProdutos == LIMITE) {
            System.out.println("Limite de produtos atingido.");
            return;
        }

        codigo = lerCodigoProduto();

        if (buscarProduto(codigo) != -1) {
            System.out.println("Produto já cadastrado.");
            return;
        }

        responsavel = lerCodigoFuncionarioExistente("Código do funcionário responsável: ");

        codigoProdutos[totalProdutos] = codigo;
        nomeProdutos[totalProdutos] = lerTextoObrigatorio("Nome: ");
        categoriaProdutos[totalProdutos] = lerTextoObrigatorio("Categoria: ");
        precoProdutos[totalProdutos] = lerPreco();
        quantidadeProdutos[totalProdutos] = lerInteiroNaoNegativo("Quantidade inicial: ");
        minimoProdutos[totalProdutos] = lerInteiroNaoNegativo("Estoque mínimo: ");
        aliquotaProdutos[totalProdutos] = lerAliquota();
        responsavelProdutos[totalProdutos] = responsavel;
        totalProdutos++;

        System.out.println("Produto cadastrado com sucesso.");
    }

    // Registra a entrada de novas unidades no estoque.
    static void registrarEntrada() {
        String codigo = lerTextoObrigatorio("Código do produto: ").toUpperCase();
        int produto = buscarProduto(codigo);

        if (produto == -1) {
            System.out.println("Produto não encontrado.");
            return;
        }

        int quantidade = lerInteiroPositivo("Quantidade de entrada: ");
        String funcionario = lerCodigoFuncionarioExistente("Código do funcionário: ");

        quantidadeProdutos[produto] = quantidadeProdutos[produto] + quantidade;
        System.out.println("Entrada registrada por " + funcionario + ".");
    }

    // Registra uma venda e diminui o estoque quando há quantidade disponível.
    static void registrarSaida() {
        String codigo = lerTextoObrigatorio("Código do produto: ").toUpperCase();
        int produto = buscarProduto(codigo);

        if (produto == -1) {
            System.out.println("Produto não encontrado.");
            return;
        }

        int quantidade = lerInteiroPositivo("Quantidade de saída: ");
        String cpf = lerCpf();

        if (buscarCliente(cpf) == -1) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        if (quantidade > quantidadeProdutos[produto]) {
            System.out.println("Estoque insuficiente.");
            return;
        }

        quantidadeProdutos[produto] = quantidadeProdutos[produto] - quantidade;
        System.out.println("Venda registrada com sucesso.");
    }

    // Lista os funcionários cadastrados.
    static void listarFuncionarios() {
        int i;

        if (totalFuncionarios == 0) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }

        System.out.println("\n--- Funcionários cadastrados ---");

        for (i = 0; i < totalFuncionarios; i++) {
            System.out.println("[" + codigoFuncionarios[i] + "] "
                    + nomeFuncionarios[i] + " | Cargo: " + cargoFuncionarios[i]);
        }
    }

    // Lista os clientes cadastrados.
    static void listarClientes() {
        int i;

        if (totalClientes == 0) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        System.out.println("\n--- Clientes cadastrados ---");

        for (i = 0; i < totalClientes; i++) {
            System.out.println("[CPF: " + cpfClientes[i] + "] "
                    + nomeClientes[i] + " | E-mail: " + emailClientes[i]);
        }
    }

    // Abre o menu de consulta do estoque.
    static void menuEstoque() {
        int opcao;

        do {
            System.out.println("\n--- Consulta de estoque ---");
            System.out.println("1 - Listar todos os produtos");
            System.out.println("2 - Listar produtos no estoque mínimo");
            System.out.println("0 - Voltar");

            opcao = lerOpcao("Opção: ");

            if (opcao == 1) {
                listarProdutos(false);
            } else if (opcao == 2) {
                listarProdutos(true);
            } else if (opcao != 0) {
                System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    // Lista todos os produtos ou somente os que chegaram ao mínimo.
    static void listarProdutos(boolean somenteMinimo) {
        int i;
        boolean encontrou = false;

        for (i = 0; i < totalProdutos; i++) {
            if (!somenteMinimo || quantidadeProdutos[i] <= minimoProdutos[i]) {
                if (!encontrou) {
                    if (somenteMinimo) {
                        System.out.println("\n--- Produtos no estoque mínimo ---");
                    } else {
                        System.out.println("\n--- Produtos em estoque ---");
                    }
                }

                exibirProduto(i);
                encontrou = true;
            }
        }

        if (!encontrou) {
            if (somenteMinimo) {
                System.out.println("Nenhum produto está no estoque mínimo.");
            } else {
                System.out.println("Nenhum produto cadastrado.");
            }
        }
    }

    // Mostra os dados de um produto.
    static void exibirProduto(int i) {
        double valorEstoque = precoProdutos[i] * quantidadeProdutos[i];
        double tributo = valorEstoque * aliquotaProdutos[i] / 100;

        System.out.println("\n[" + codigoProdutos[i] + "] " + nomeProdutos[i]);
        System.out.println("Categoria: " + categoriaProdutos[i]);
        System.out.println("Estoque: " + quantidadeProdutos[i]
                + " | Mínimo: " + minimoProdutos[i]);
        System.out.println("Responsável: " + responsavelProdutos[i]);
        System.out.printf("Preço: R$ %.2f%n", precoProdutos[i]);
        System.out.printf("Alíquota: %.2f%%%n", aliquotaProdutos[i]);
        System.out.printf("Tributo estimado: R$ %.2f%n", tributo);
    }

    // Abre o menu de relatórios e de alteração do regime tributário.
    static void menuRelatorios() {
        int opcao;

        do {
            System.out.println("\n--- Relatórios e regime tributário ---");
            System.out.println("1 - Relatório tributável");
            System.out.println("2 - Alterar regime tributário");
            System.out.println("0 - Voltar");

            opcao = lerOpcao("Opção: ");

            if (opcao == 1) {
                mostrarRelatorio();
            } else if (opcao == 2) {
                regime = lerRegime();
            } else if (opcao != 0) {
                System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    // Calcula os valores do estoque e os tributos estimados.
    static void mostrarRelatorio() {
        int i;
        int noMinimo = 0;
        double valor = 0;
        double tributo = 0;

        for (i = 0; i < totalProdutos; i++) {
            valor = valor + precoProdutos[i] * quantidadeProdutos[i];
            tributo = tributo + precoProdutos[i] * quantidadeProdutos[i]
                    * aliquotaProdutos[i] / 100;

            if (quantidadeProdutos[i] <= minimoProdutos[i]) {
                noMinimo++;
            }
        }

        System.out.println("\n--- Relatório de Estoque Tributável ---");
        System.out.println("Regime: " + nomeRegime());
        System.out.println("As alíquotas são informadas para cada produto.");
        System.out.printf("Valor dos produtos: R$ %.2f%n", valor);
        System.out.printf("Tributos estimados: R$ %.2f%n", tributo);
        System.out.printf("Valor total com tributos: R$ %.2f%n", valor + tributo);
        System.out.println("Itens no estoque mínimo: " + noMinimo);
    }

    // Lê o regime tributário escolhido.
    static int lerRegime() {
        int opcao;

        do {
            System.out.println("\n1 - Simples Nacional");
            System.out.println("2 - Lucro Presumido");
            System.out.println("3 - Lucro Real");

            opcao = lerOpcao("Escolha o regime: ");

            if (opcao < 1 || opcao > 3) {
                System.out.println("Escolha 1, 2 ou 3.");
            }
        } while (opcao < 1 || opcao > 3);

        return opcao;
    }

    // Retorna o nome do regime selecionado.
    static String nomeRegime() {
        if (regime == 1) {
            return "Simples Nacional";
        } else if (regime == 2) {
            return "Lucro Presumido";
        }

        return "Lucro Real";
    }

    // Lê uma opção formada por apenas um número de 0 a 9.
    static int lerOpcao(String mensagem) {
        String texto;

        do {
            System.out.print(mensagem);
            texto = entrada.nextLine().trim();

            if (!somenteNumeros(texto) || texto.length() != 1) {
                System.out.println("Digite apenas um algarismo de 0 a 9.");
            }
        } while (!somenteNumeros(texto) || texto.length() != 1);

        return texto.charAt(0) - '0';
    }

    // Lê um texto que não pode ficar vazio e tem tamanho limitado.
    static String lerTextoObrigatorio(String mensagem) {
        String texto;

        do {
            System.out.print(mensagem);
            texto = entrada.nextLine().trim();

            if (texto.length() < 2 || texto.length() > 80) {
                System.out.println("Digite um texto entre 2 e 80 caracteres.");
            }
        } while (texto.length() < 2 || texto.length() > 80);

        return texto;
    }

    // Lê um inteiro que pode ser zero, mas não pode ser negativo.
    static int lerInteiroNaoNegativo(String mensagem) {
        String texto;

        do {
            System.out.print(mensagem);
            texto = entrada.nextLine().trim();

            if (!somenteNumeros(texto) || texto.length() > 9) {
                System.out.println("Digite apenas números, com no máximo 9 algarismos.");
            }
        } while (!somenteNumeros(texto) || texto.length() > 9);

        return converterParaInteiro(texto);
    }

    // Lê um inteiro obrigatoriamente maior que zero.
    static int lerInteiroPositivo(String mensagem) {
        int numero;

        do {
            numero = lerInteiroNaoNegativo(mensagem);

            if (numero == 0) {
                System.out.println("O valor deve ser maior que zero.");
            }
        } while (numero == 0);

        return numero;
    }

    // Lê o preço do produto aceitando vírgula ou ponto.
    static double lerPreco() {
        String texto;
        double preco;

        do {
            System.out.print("Preço unitário (ex.: 19,90): ");
            texto = entrada.nextLine().trim();

            if (!numeroDecimalValido(texto)) {
                System.out.println("Digite um preço válido.");
                preco = 0;
            } else {
                preco = converterParaReal(texto);

                if (preco <= 0) {
                    System.out.println("O preço deve ser maior que zero.");
                }
            }
        } while (!numeroDecimalValido(texto) || preco <= 0);

        return preco;
    }

    // Lê a alíquota tributária do produto.
    static double lerAliquota() {
        String texto;
        double aliquota;

        do {
            System.out.print("Alíquota tributária do produto (%): ");
            texto = entrada.nextLine().trim();

            if (!numeroDecimalValido(texto)) {
                System.out.println("Digite uma alíquota válida.");
                aliquota = -1;
            } else {
                aliquota = converterParaReal(texto);

                if (aliquota < 0 || aliquota > 100) {
                    System.out.println("A alíquota deve ficar entre 0 e 100.");
                }
            }
        } while (!numeroDecimalValido(texto) || aliquota < 0 || aliquota > 100);

        return aliquota;
    }

    // Lê o CPF e só aceita 11 números que passam no cálculo de validação.
    static String lerCpf() {
        String cpf;

        do {
            System.out.print("CPF do cliente (11 números): ");
            cpf = entrada.nextLine().trim();

            if (!cpfValido(cpf)) {
                System.out.println("CPF inválido. Digite somente um CPF válido com 11 números.");
            }
        } while (!cpfValido(cpf));

        return cpf;
    }

    // Lê e confere o formato básico do e-mail.
    static String lerEmail() {
        String email;

        do {
            System.out.print("E-mail: ");
            email = entrada.nextLine().trim();

            if (!emailValido(email)) {
                System.out.println("Use o formato nome@dominio.com.");
            }
        } while (!emailValido(email));

        return email;
    }

    // Lê um código de funcionário no formato FUN-001.
    static String lerCodigoFuncionario() {
        String codigo;

        do {
            System.out.print("Código do funcionário (FUN-001): ");
            codigo = entrada.nextLine().trim().toUpperCase();

            if (!codigoFuncionarioValido(codigo)) {
                System.out.println("Use o formato FUN-001 até FUN-999999.");
            }
        } while (!codigoFuncionarioValido(codigo));

        return codigo;
    }

    // Lê um funcionário e confirma se ele já foi cadastrado.
    static String lerCodigoFuncionarioExistente(String mensagem) {
        String codigo;

        do {
            System.out.print(mensagem);
            codigo = entrada.nextLine().trim().toUpperCase();

            if (buscarFuncionario(codigo) == -1) {
                System.out.println("Funcionário não encontrado.");
            }
        } while (buscarFuncionario(codigo) == -1);

        return codigo;
    }

    // Lê um código simples para identificar o produto.
    static String lerCodigoProduto() {
        String codigo;

        do {
            System.out.print("Código do produto: ");
            codigo = entrada.nextLine().trim().toUpperCase();

            if (codigo.length() < 3 || codigo.length() > 20 || codigo.indexOf(' ') != -1) {
                System.out.println("O código deve ter de 3 a 20 caracteres e não pode ter espaço.");
            }
        } while (codigo.length() < 3 || codigo.length() > 20 || codigo.indexOf(' ') != -1);

        return codigo;
    }

    // Verifica se a cadeia contém somente algarismos.
    static boolean somenteNumeros(String texto) {
        int i;

        if (texto.length() == 0) {
            return false;
        }

        for (i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) < '0' || texto.charAt(i) > '9') {
                return false;
            }
        }

        return true;
    }

    // Confere se o texto possui um número decimal simples.
    static boolean numeroDecimalValido(String texto) {
        int i;
        int separadores = 0;

        if (texto.length() == 0 || texto.length() > 12) {
            return false;
        }

        for (i = 0; i < texto.length(); i++) {
            char caractere = texto.charAt(i);

            if (caractere == ',' || caractere == '.') {
                separadores++;

                if (separadores > 1 || i == 0 || i == texto.length() - 1) {
                    return false;
                }
            } else if (caractere < '0' || caractere > '9') {
                return false;
            }
        }

        return true;
    }

    // Transforma uma cadeia de números em inteiro usando um laço.
    static int converterParaInteiro(String texto) {
        int i;
        int numero = 0;

        for (i = 0; i < texto.length(); i++) {
            numero = numero * 10 + (texto.charAt(i) - '0');
        }

        return numero;
    }

    // Transforma um texto decimal em número real.
    static double converterParaReal(String texto) {
        int i;
        boolean depoisDaVirgula = false;
        double numero = 0;
        double divisor = 10;

        for (i = 0; i < texto.length(); i++) {
            char caractere = texto.charAt(i);

            if (caractere == ',' || caractere == '.') {
                depoisDaVirgula = true;
            } else if (!depoisDaVirgula) {
                numero = numero * 10 + (caractere - '0');
            } else {
                numero = numero + (caractere - '0') / divisor;
                divisor = divisor * 10;
            }
        }

        return numero;
    }

    // Confere o padrão do código do funcionário.
    static boolean codigoFuncionarioValido(String codigo) {
        int i;

        if (codigo.length() < 7 || codigo.length() > 10) {
            return false;
        }

        if (codigo.charAt(0) != 'F' || codigo.charAt(1) != 'U'
                || codigo.charAt(2) != 'N' || codigo.charAt(3) != '-') {
            return false;
        }

        for (i = 4; i < codigo.length(); i++) {
            if (codigo.charAt(i) < '0' || codigo.charAt(i) > '9') {
                return false;
            }
        }

        return true;
    }

    // Faz uma validação simples para e-mail.
    static boolean emailValido(String email) {
        int i;
        int arroba = -1;
        int ponto = -1;

        if (email.length() < 6 || email.length() > 80) {
            return false;
        }

        for (i = 0; i < email.length(); i++) {
            if (email.charAt(i) == ' ') {
                return false;
            }

            if (email.charAt(i) == '@') {
                arroba = i;
            }

            if (email.charAt(i) == '.' && i > arroba) {
                ponto = i;
            }
        }

        return arroba > 0 && ponto > arroba + 1 && ponto < email.length() - 1;
    }

    // Calcula os dois últimos dígitos para validar o CPF.
    static boolean cpfValido(String cpf) {
        int i;
        int soma;
        int primeiro;
        int segundo;
        boolean todosIguais = true;

        if (cpf.length() != 11 || !somenteNumeros(cpf)) {
            return false;
        }

        for (i = 1; i < 11; i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                todosIguais = false;
            }
        }

        if (todosIguais) {
            return false;
        }

        soma = 0;
        for (i = 0; i < 9; i++) {
            soma = soma + (cpf.charAt(i) - '0') * (10 - i);
        }

        if (soma % 11 < 2) {
            primeiro = 0;
        } else {
            primeiro = 11 - (soma % 11);
        }

        soma = 0;
        for (i = 0; i < 10; i++) {
            soma = soma + (cpf.charAt(i) - '0') * (11 - i);
        }

        if (soma % 11 < 2) {
            segundo = 0;
        } else {
            segundo = 11 - (soma % 11);
        }

        return cpf.charAt(9) - '0' == primeiro && cpf.charAt(10) - '0' == segundo;
    }

    // Procura um cliente pelo CPF e devolve sua posição no vetor.
    static int buscarCliente(String cpf) {
        int i;

        for (i = 0; i < totalClientes; i++) {
            if (cpfClientes[i].equals(cpf)) {
                return i;
            }
        }

        return -1;
    }

    // Procura um funcionário pelo código e devolve sua posição no vetor.
    static int buscarFuncionario(String codigo) {
        int i;

        for (i = 0; i < totalFuncionarios; i++) {
            if (codigoFuncionarios[i].equals(codigo)) {
                return i;
            }
        }

        return -1;
    }

    // Procura um produto pelo código e devolve sua posição no vetor.
    static int buscarProduto(String codigo) {
        int i;

        for (i = 0; i < totalProdutos; i++) {
            if (codigoProdutos[i].equals(codigo)) {
                return i;
            }
        }

        return -1;
    }
}
