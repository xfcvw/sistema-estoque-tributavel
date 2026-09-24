import java.util.Scanner;

/** Sistema procedural: usa vetores e métodos static, sem POO. */
public class Main {
    private static final Scanner ENTRADA = new Scanner(System.in);
    private static final int LIMITE = 100;

    // Vetores paralelos de clientes.
    private static final String[] cpfClientes = new String[LIMITE];
    private static final String[] nomeClientes = new String[LIMITE];
    private static final String[] emailClientes = new String[LIMITE];
    private static int totalClientes;

    // Vetores paralelos de funcionários.
    private static final String[] codigoFuncionarios = new String[LIMITE];
    private static final String[] nomeFuncionarios = new String[LIMITE];
    private static final String[] cargoFuncionarios = new String[LIMITE];
    private static int totalFuncionarios;

    // Vetores paralelos de produtos.
    private static final String[] codigoProdutos = new String[LIMITE];
    private static final String[] nomeProdutos = new String[LIMITE];
    private static final String[] categoriaProdutos = new String[LIMITE];
    private static final double[] precoProdutos = new double[LIMITE];
    private static final int[] quantidadeProdutos = new int[LIMITE];
    private static final int[] minimoProdutos = new int[LIMITE];
    private static final String[] responsavelProdutos = new String[LIMITE];
    private static int totalProdutos;

    // 1 = Simples Nacional, 2 = Lucro Presumido, 3 = Lucro Real.
    private static int regime;

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("--teste")) {
            executarTestes();
            return;
        }

        System.out.println("=== Sistema de Gerenciamento de Estoque Tributável ===");
        regime = lerRegime();

        int opcao = -1;
        do {
            mostrarMenu();
            try {
                opcao = lerOpcao("Opção: ");
                executarOpcao(opcao);
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
                opcao = -1;
            }
        } while (opcao != 0);

        System.out.println("Sistema encerrado.");
    }

    private static void mostrarMenu() {
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

    private static void executarOpcao(int opcao) {
        switch (opcao) {
            case 1: cadastrarFuncionario(); break;
            case 2: cadastrarCliente(); break;
            case 3: listarFuncionarios(); break;
            case 4: listarClientes(); break;
            case 5: cadastrarProduto(); break;
            case 6: registrarEntrada(); break;
            case 7: registrarSaida(); break;
            case 8: menuEstoque(); break;
            case 9: menuRelatorios(); break;
            case 0: break;
            default: System.out.println("Opção inválida.");
        }
    }

    private static void cadastrarFuncionario() {
        verificarLimite(totalFuncionarios, "funcionários");
        String codigo = lerTexto("Código do funcionário (FUN-001): ").toUpperCase();
        validarCodigoFuncionario(codigo);
        if (buscarFuncionario(codigo) != -1) throw new IllegalArgumentException("Funcionário já cadastrado.");

        codigoFuncionarios[totalFuncionarios] = codigo;
        nomeFuncionarios[totalFuncionarios] = lerTextoObrigatorio("Nome: ");
        cargoFuncionarios[totalFuncionarios] = lerTextoObrigatorio("Cargo: ");
        totalFuncionarios++;
        System.out.println("Funcionário cadastrado com sucesso.");
    }

    private static void cadastrarCliente() {
        verificarLimite(totalClientes, "clientes");
        String cpf = lerCpf("CPF do cliente (11 números): ");
        if (buscarCliente(cpf) != -1) throw new IllegalArgumentException("Cliente já cadastrado.");

        cpfClientes[totalClientes] = cpf;
        nomeClientes[totalClientes] = lerTextoObrigatorio("Nome: ");
        emailClientes[totalClientes] = lerEmail();
        totalClientes++;
        System.out.println("Cliente cadastrado com sucesso.");
    }

    private static void cadastrarProduto() {
        verificarLimite(totalProdutos, "produtos");
        String codigo = lerTexto("Código do produto: ").toUpperCase();
        validarCodigoProduto(codigo);
        if (buscarProduto(codigo) != -1) throw new IllegalArgumentException("Produto já cadastrado.");

        String responsavel = lerTexto("Código do funcionário responsável: ").toUpperCase();
        if (buscarFuncionario(responsavel) == -1) throw new IllegalArgumentException("Funcionário não encontrado.");

        codigoProdutos[totalProdutos] = codigo;
        nomeProdutos[totalProdutos] = lerTextoObrigatorio("Nome: ");
        categoriaProdutos[totalProdutos] = lerTextoObrigatorio("Categoria: ");
        precoProdutos[totalProdutos] = lerPreco();
        quantidadeProdutos[totalProdutos] = lerInteiroNaoNegativo("Quantidade inicial: ");
        minimoProdutos[totalProdutos] = lerInteiroNaoNegativo("Estoque mínimo: ");
        responsavelProdutos[totalProdutos] = responsavel;
        totalProdutos++;
        System.out.println("Produto cadastrado com sucesso.");
    }

    private static void registrarEntrada() {
        int produto = buscarProduto(lerTexto("Código do produto: ").toUpperCase());
        if (produto == -1) throw new IllegalArgumentException("Produto não encontrado.");
        int quantidade = lerInteiroPositivo("Quantidade de entrada: ");
        String funcionario = lerTexto("Código do funcionário: ").toUpperCase();
        if (buscarFuncionario(funcionario) == -1) throw new IllegalArgumentException("Funcionário não encontrado.");
        if (quantidadeProdutos[produto] > Integer.MAX_VALUE - quantidade) throw new IllegalArgumentException("Quantidade muito alta.");

        quantidadeProdutos[produto] += quantidade;
        System.out.println("Entrada registrada com sucesso.");
    }

    private static void registrarSaida() {
        int produto = buscarProduto(lerTexto("Código do produto: ").toUpperCase());
        if (produto == -1) throw new IllegalArgumentException("Produto não encontrado.");
        int quantidade = lerInteiroPositivo("Quantidade de saída: ");
        String cpf = lerCpf("CPF do cliente (11 números): ");
        if (buscarCliente(cpf) == -1) throw new IllegalArgumentException("Cliente não encontrado.");
        if (!podeRetirar(quantidadeProdutos[produto], quantidade)) {
            throw new IllegalArgumentException("Estoque insuficiente.");
        }

        quantidadeProdutos[produto] -= quantidade;
        System.out.println("Saída/venda registrada com sucesso.");
    }

    private static void listarFuncionarios() {
        if (totalFuncionarios == 0) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }
        System.out.println("\n--- Funcionários cadastrados ---");
        for (int i = 0; i < totalFuncionarios; i++) {
            System.out.printf("[%s] %s | Cargo: %s%n", codigoFuncionarios[i], nomeFuncionarios[i], cargoFuncionarios[i]);
        }
    }

    private static void listarClientes() {
        if (totalClientes == 0) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        System.out.println("\n--- Clientes cadastrados ---");
        for (int i = 0; i < totalClientes; i++) {
            System.out.printf("[CPF: %s] %s | E-mail: %s%n", cpfClientes[i], nomeClientes[i], emailClientes[i]);
        }
    }

    private static void menuEstoque() {
        int opcao = -1;
        do {
            System.out.println("\n--- Consulta de estoque ---");
            System.out.println("1 - Listar todos os produtos");
            System.out.println("2 - Listar produtos no estoque mínimo");
            System.out.println("0 - Voltar");
            try {
                opcao = lerOpcao("Opção: ");
                if (opcao == 1) listarProdutos(false);
                else if (opcao == 2) listarProdutos(true);
                else if (opcao != 0) System.out.println("Opção inválida.");
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
                opcao = -1;
            }
        } while (opcao != 0);
    }

    /** Mostra todos os produtos ou somente os que chegaram ao estoque mínimo. */
    private static void listarProdutos(boolean somenteMinimo) {
        boolean encontrou = false;
        for (int i = 0; i < totalProdutos; i++) {
            if (!somenteMinimo || estaNoMinimo(quantidadeProdutos[i], minimoProdutos[i])) {
                if (!encontrou) System.out.println(somenteMinimo ? "\n--- Produtos no estoque mínimo ---" : "\n--- Produtos em estoque ---");
                exibirProduto(i);
                encontrou = true;
            }
        }
        if (!encontrou) System.out.println(somenteMinimo ? "Nenhum produto está no estoque mínimo." : "Nenhum produto cadastrado.");
    }

    private static void exibirProduto(int i) {
        double valorEstoque = precoProdutos[i] * quantidadeProdutos[i];
        System.out.printf("%n[%s] %s | Categoria: %s%n", codigoProdutos[i], nomeProdutos[i], categoriaProdutos[i]);
        System.out.printf("Estoque: %d (mínimo: %d)%n", quantidadeProdutos[i], minimoProdutos[i]);
        System.out.println("Cadastrado por: " + responsavelProdutos[i]);
        System.out.printf("Preço: R$ %.2f%n", precoProdutos[i]);
        System.out.printf("Tributo estimado (%s): R$ %.2f%n", nomeRegime(), valorEstoque * aliquota() / 100);
    }

    private static void menuRelatorios() {
        int opcao = -1;
        do {
            System.out.println("\n--- Relatórios e regime tributário ---");
            System.out.println("1 - Relatório tributável");
            System.out.println("2 - Alterar regime tributário");
            System.out.println("0 - Voltar");
            try {
                opcao = lerOpcao("Opção: ");
                if (opcao == 1) mostrarRelatorio();
                else if (opcao == 2) regime = lerRegime();
                else if (opcao != 0) System.out.println("Opção inválida.");
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
                opcao = -1;
            }
        } while (opcao != 0);
    }

    private static void mostrarRelatorio() {
        double valor = 0;
        int noMinimo = 0;
        for (int i = 0; i < totalProdutos; i++) {
            valor += precoProdutos[i] * quantidadeProdutos[i];
            if (estaNoMinimo(quantidadeProdutos[i], minimoProdutos[i])) noMinimo++;
        }
        double tributo = valor * aliquota() / 100;
        System.out.println("\n--- Relatório de Estoque Tributável ---");
        System.out.println("Regime: " + nomeRegime());
        System.out.printf("Alíquota estimada: %.2f%%%n", aliquota());
        System.out.printf("Valor dos produtos: R$ %.2f%n", valor);
        System.out.printf("Tributos estimados: R$ %.2f%n", tributo);
        System.out.printf("Valor total com tributos: R$ %.2f%n", valor + tributo);
        System.out.println("Itens no mínimo/abaixo: " + noMinimo);
    }

    private static int lerRegime() {
        while (true) {
            System.out.println("\n1 - Simples Nacional | 2 - Lucro Presumido | 3 - Lucro Real");
            int opcao = lerOpcao("Escolha o regime: ");
            if (opcao >= 1 && opcao <= 3) return opcao;
            System.out.println("Escolha 1, 2 ou 3.");
        }
    }

    private static String nomeRegime() {
        if (regime == 1) return "Simples Nacional";
        if (regime == 2) return "Lucro Presumido";
        return "Lucro Real";
    }

    private static double aliquota() {
        if (regime == 1) return 6.00;
        if (regime == 2) return 11.33;
        return 15.00;
    }

    private static int buscarCliente(String cpf) {
        for (int i = 0; i < totalClientes; i++) if (cpfClientes[i].equals(cpf)) return i;
        return -1;
    }

    private static int buscarFuncionario(String codigo) {
        for (int i = 0; i < totalFuncionarios; i++) if (codigoFuncionarios[i].equals(codigo)) return i;
        return -1;
    }

    private static int buscarProduto(String codigo) {
        for (int i = 0; i < totalProdutos; i++) if (codigoProdutos[i].equals(codigo)) return i;
        return -1;
    }

    /** Confere se a saída é positiva e não ultrapassa a quantidade disponível. */
    private static boolean podeRetirar(int quantidadeDisponivel, int quantidadeSaida) {
        return quantidadeSaida > 0 && quantidadeSaida <= quantidadeDisponivel;
    }

    private static boolean estaNoMinimo(int quantidadeAtual, int estoqueMinimo) {
        return quantidadeAtual <= estoqueMinimo;
    }

    private static String lerTexto(String rotulo) {
        System.out.print(rotulo);
        return ENTRADA.nextLine().trim();
    }

    private static String lerTextoObrigatorio(String rotulo) {
        String texto = lerTexto(rotulo);
        if (texto.length() < 2 || texto.length() > 80) throw new IllegalArgumentException("Texto deve ter entre 2 e 80 caracteres.");
        return texto;
    }

    /** Repete a pergunta até receber 11 números válidos, sem letras ou símbolos. */
    private static String lerCpf(String rotulo) {
        while (true) {
            String cpf = lerTexto(rotulo);
            if (cpf.matches("[0-9]{11}") && cpfValido(cpf)) return cpf;
            System.out.println("CPF inválido. Digite somente 11 números, sem letras ou símbolos.");
        }
    }

    private static String lerEmail() {
        String email = lerTexto("E-mail: ");
        if (!email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) throw new IllegalArgumentException("E-mail inválido.");
        return email;
    }

    private static int lerOpcao(String rotulo) {
        String texto = lerTexto(rotulo);
        if (!texto.matches("[0-9]")) throw new IllegalArgumentException("A opção deve ter somente um algarismo de 0 a 9.");
        return Integer.parseInt(texto);
    }

    private static int lerInteiroNaoNegativo(String rotulo) {
        int numero = lerInteiro(rotulo);
        if (numero < 0) throw new IllegalArgumentException("O valor não pode ser negativo.");
        return numero;
    }

    private static int lerInteiroPositivo(String rotulo) {
        int numero = lerInteiro(rotulo);
        if (numero <= 0) throw new IllegalArgumentException("O valor deve ser maior que zero.");
        return numero;
    }

    private static int lerInteiro(String rotulo) {
        String texto = lerTexto(rotulo);
        if (!texto.matches("-?[0-9]{1,9}")) throw new IllegalArgumentException("Digite um inteiro com no máximo 9 algarismos.");
        return Integer.parseInt(texto);
    }

    private static double lerPreco() {
        try {
            double preco = Double.parseDouble(lerTexto("Preço unitário (ex.: 19,90): ").replace(',', '.'));
            if (preco <= 0 || Double.isInfinite(preco) || Double.isNaN(preco)) throw new IllegalArgumentException("Preço deve ser maior que zero.");
            return preco;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Digite um preço válido.");
        }
    }

    private static void validarCodigoFuncionario(String codigo) {
        if (!codigo.matches("FUN-[0-9]{3,6}")) throw new IllegalArgumentException("Use o formato FUN-001 até FUN-999999.");
    }

    private static void validarCodigoProduto(String codigo) {
        if (!codigo.matches("[A-Z0-9-]{3,20}")) throw new IllegalArgumentException("Código do produto deve ter 3 a 20 caracteres.");
    }

    private static void verificarLimite(int total, String tipo) {
        if (total >= LIMITE) throw new IllegalArgumentException("Limite de " + LIMITE + " " + tipo + " atingido.");
    }

    private static boolean cpfValido(String cpf) {
        if (!cpf.matches("[0-9]{11}") || cpf.matches("([0-9])\\1{10}")) return false;
        int soma = 0;
        for (int i = 0; i < 9; i++) soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        int primeiro = soma % 11 < 2 ? 0 : 11 - soma % 11;
        soma = 0;
        for (int i = 0; i < 10; i++) soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        int segundo = soma % 11 < 2 ? 0 : 11 - soma % 11;
        return cpf.charAt(9) == Character.forDigit(primeiro, 10) && cpf.charAt(10) == Character.forDigit(segundo, 10);
    }

    /** Testes simples: execute com java -cp out Main --teste. */
    private static void executarTestes() {
        int aprovados = 0;
        verificar(!cpfValido("1234567890A"), "CPF com letra deve ser bloqueado"); aprovados++;
        verificar(!cpfValido("123.456.789-09"), "CPF com símbolo deve ser bloqueado"); aprovados++;
        verificar(cpfValido("12345678909"), "CPF válido deveria ser aceito"); aprovados++;
        verificar(!"10".matches("[0-9]"), "Opção com dois caracteres deve ser bloqueada"); aprovados++;
        verificar(!podeRetirar(3, 7), "Saída maior que estoque deve ser bloqueada"); aprovados++;
        verificar(estaNoMinimo(3, 3), "Produto no mínimo deve aparecer na consulta"); aprovados++;
        System.out.println("Todos os " + aprovados + " testes passaram.");
    }

    private static void verificar(boolean condicao, String mensagem) {
        if (!condicao) throw new AssertionError(mensagem);
    }
}
