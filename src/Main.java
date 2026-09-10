import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Ponto de entrada: mostra o menu e conversa com a pessoa pelo terminal. */
public class Main {
    private static final Scanner ENTRADA = new Scanner(System.in);
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static EstoqueTributavel estoque;

    public static void main(String[] args) {
        System.out.println("=== Sistema de Gerenciamento de Estoque Tributável ===");

        try {
            estoque = new EstoqueTributavel(lerRegimeTributario());
            int opcao = -1;

            // O menu só encerra quando a pessoa escolhe a opção 0.
            do {
                mostrarMenu();

                try {
                    opcao = lerOpcao("Opção: ");
                    executarOpcao(opcao);
                } catch (IllegalArgumentException e) {
                    System.out.println("\nErro: " + e.getMessage());
                    // Mantém o menu aberto depois de uma opção ou dado inválido.
                    opcao = -1;
                }
            } while (opcao != 0);

            System.out.println("Sistema encerrado.");
        } catch (EntradaEncerradaException e) {
            // Trata Ctrl+D ou fim de um arquivo de entrada sem mostrar erro técnico.
            System.out.println("\nEntrada encerrada. Sistema finalizado.");
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n1 - Cadastrar funcionário");
        System.out.println("2 - Cadastrar cliente");
        System.out.println("3 - Cadastrar produto");
        System.out.println("4 - Registrar entrada (funcionário)");
        System.out.println("5 - Registrar saída/venda (cliente)");
        System.out.println("6 - Listar estoque");
        System.out.println("7 - Produtos no estoque mínimo");
        System.out.println("8 - Relatório tributável");
        System.out.println("9 - Alterar regime tributário");
        System.out.println("0 - Sair");
    }

    /** Direciona a opção escolhida para o método correspondente. */
    private static void executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> cadastrarFuncionario();
            case 2 -> cadastrarCliente();
            case 3 -> cadastrarProduto();
            case 4 -> registrarEntrada();
            case 5 -> registrarSaidaParaCliente();
            case 6 -> listar(estoque.listarProdutos());
            case 7 -> listar(estoque.listarAbaixoDoMinimo());
            case 8 -> relatorio();
            case 9 -> alterarRegimeTributario();
            case 0 -> {
                // Não executa nada: o laço principal terminará.
            }
            default -> System.out.println("Opção inválida. Escolha uma opção do menu.");
        }
    }

    private static void cadastrarProduto() {
        Produto produto = new Produto(
                lerTexto("Código: "),
                lerTexto("Nome: "),
                lerTexto("Categoria: "),
                lerDecimal("Preço unitário (ex.: 19,90): "),
                lerInteiroNaoNegativo("Quantidade inicial: "),
                lerInteiroNaoNegativo("Estoque mínimo: "),
                lerTexto("Código do funcionário responsável (ex.: FUN-001): ")
        );

        estoque.cadastrarProduto(produto);
        System.out.println("Produto cadastrado com sucesso.");
    }

    private static void cadastrarFuncionario() {
        Funcionario funcionario = new Funcionario(
                lerTexto("Código do funcionário (ex.: FUN-001): "),
                lerTexto("Nome: "),
                lerTexto("Cargo: ")
        );

        estoque.cadastrarFuncionario(funcionario);
        System.out.println("Funcionário cadastrado com sucesso.");
    }

    private static void cadastrarCliente() {
        Cliente cliente = new Cliente(
                lerTexto("Código do cliente (ex.: CLI-001): "),
                lerTexto("Nome: "),
                lerTexto("E-mail: ")
        );

        estoque.cadastrarCliente(cliente);
        System.out.println("Cliente cadastrado com sucesso.");
    }

    /** Registra uma entrada e identifica o funcionário que a realizou. */
    private static void registrarEntrada() {
        String codigo = lerTexto("Código: ");
        int quantidade = lerInteiro("Quantidade: ");
        String codigoFuncionario = lerTexto("Código do funcionário: ");

        estoque.registrarEntrada(codigo, quantidade, codigoFuncionario);
        System.out.println("Entrada registrada com sucesso.");
    }

    /** Registra uma saída apenas para um cliente previamente cadastrado. */
    private static void registrarSaidaParaCliente() {
        String codigo = lerTexto("Código do produto: ");
        int quantidade = lerInteiro("Quantidade: ");
        String codigoCliente = lerTexto("Código do cliente: ");

        estoque.registrarSaidaParaCliente(codigo, quantidade, codigoCliente);
        System.out.println("Saída/venda registrada com sucesso.");
    }

    private static void listar(List<Produto> produtos) {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        produtos.forEach(Main::exibirProduto);
    }

    /** Mostra no terminal os dados relevantes de um produto. */
    private static void exibirProduto(Produto produto) {
        System.out.printf("%n[%s] %s | Categoria: %s%n", produto.getCodigo(), produto.getNome(), produto.getCategoria());
        System.out.printf("Estoque: %d (mínimo: %d)%n", produto.getQuantidadeEmEstoque(), produto.getEstoqueMinimo());
        System.out.println("Cadastrado por: " + produto.getCodigoFuncionarioResponsavel());
        System.out.printf("Preço: %s%n", MOEDA.format(produto.getPrecoUnitario()));
        System.out.printf(
                "Tributo estimado (%s): %s%n",
                estoque.getRegimeTributario().getDescricao(),
                MOEDA.format(estoque.calcularTributoDoProduto(produto))
        );
    }

    private static void relatorio() {
        System.out.println("\n--- Relatório de Estoque Tributável ---");
        System.out.println("Regime: " + estoque.getRegimeTributario().getDescricao());
        System.out.println("Alíquota estimada: " + estoque.getRegimeTributario().getAliquotaEstimada() + "%");
        System.out.println("Valor dos produtos: " + MOEDA.format(estoque.getValorTotalSemTributo()));
        System.out.println("Tributos estimados: " + MOEDA.format(estoque.getTributoTotalEstimado()));
        System.out.println("Valor total com tributos: " + MOEDA.format(estoque.getValorTotalComTributo()));
        System.out.println("Itens no mínimo/abaixo: " + estoque.listarAbaixoDoMinimo().size());
        System.out.println("Clientes cadastrados: " + estoque.listarClientes().size());
        System.out.println("Funcionários cadastrados: " + estoque.listarFuncionarios().size());
    }

    /** Mostra os três regimes e devolve o escolhido pela pessoa usuária. */
    private static RegimeTributario lerRegimeTributario() {
        while (true) {
            System.out.println("\n--- Regime tributário (simulação acadêmica) ---");

            for (RegimeTributario regime : RegimeTributario.values()) {
                System.out.printf(
                        "%d - %s (%s%% estimado)%n",
                        regime.getOpcao(),
                        regime.getDescricao(),
                        regime.getAliquotaEstimada().toPlainString()
                );
            }

            try {
                return RegimeTributario.porOpcao(lerOpcao("Escolha o regime: "));
            } catch (IllegalArgumentException e) {
                // Uma escolha inválida não encerra o programa; o menu é exibido novamente.
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void alterarRegimeTributario() {
        estoque.definirRegimeTributario(lerRegimeTributario());
        System.out.println("Regime tributário alterado com sucesso.");
    }

    private static String lerTexto(String rotulo) {
        System.out.print(rotulo);
        return proximaLinha();
    }

    private static int lerInteiroNaoNegativo(String rotulo) {
        int valor = lerInteiro(rotulo);

        if (valor < 0) {
            throw new IllegalArgumentException("O valor não pode ser negativo.");
        }

        return valor;
    }

    /** Lê e valida um número inteiro digitado pela pessoa. */
    private static int lerInteiro(String rotulo) {
        System.out.print(rotulo);
        String valor = proximaLinha().trim();

        if (!valor.matches("-?[0-9]{1,9}")) {
            throw new IllegalArgumentException("Digite um número inteiro com no máximo 9 algarismos.");
        }

        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Digite um número inteiro válido.");
        }
    }

    /** Opções do menu aceitam somente um algarismo de 0 a 9. */
    private static int lerOpcao(String rotulo) {
        System.out.print(rotulo);
        String valor = proximaLinha().trim();

        if (!valor.matches("[0-9]")) {
            throw new IllegalArgumentException(
                    "A opção deve conter somente um algarismo de 0 a 9."
            );
        }

        return Integer.parseInt(valor);
    }

    /** Aceita vírgula ou ponto no número decimal, comum em valores brasileiros. */
    private static BigDecimal lerDecimal(String rotulo) {
        System.out.print(rotulo);
        String valor = proximaLinha().trim().replace(',', '.');

        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Digite um valor decimal válido.");
        }
    }

    /** Evita que o programa termine com exceção técnica quando a entrada é fechada. */
    private static String proximaLinha() {
        if (!ENTRADA.hasNextLine()) {
            throw new EntradaEncerradaException();
        }

        return ENTRADA.nextLine();
    }

    private static final class EntradaEncerradaException extends RuntimeException {
    }
}
