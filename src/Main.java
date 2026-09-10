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
            int opcao;

            // O menu só encerra quando a pessoa escolhe a opção 0.
            do {
                mostrarMenu();
                opcao = lerInteiro("Opção: ");

                try {
                    executarOpcao(opcao);
                } catch (IllegalArgumentException e) {
                    System.out.println("\nErro: " + e.getMessage());
                }
            } while (opcao != 0);

            System.out.println("Sistema encerrado.");
        } catch (EntradaEncerradaException e) {
            // Trata Ctrl+D ou fim de um arquivo de entrada sem mostrar erro técnico.
            System.out.println("\nEntrada encerrada. Sistema finalizado.");
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n1 - Cadastrar produto");
        System.out.println("2 - Registrar entrada");
        System.out.println("3 - Registrar saída");
        System.out.println("4 - Consultar produto");
        System.out.println("5 - Listar estoque");
        System.out.println("6 - Produtos no estoque mínimo");
        System.out.println("7 - Relatório tributável");
        System.out.println("8 - Alterar regime tributário");
        System.out.println("0 - Sair");
    }

    /** Direciona a opção escolhida para o método correspondente. */
    private static void executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> cadastrarProduto();
            case 2 -> movimentar(true);
            case 3 -> movimentar(false);
            case 4 -> exibirProduto(estoque.buscarPorCodigo(lerTexto("Código: ")));
            case 5 -> listar(estoque.listarProdutos());
            case 6 -> listar(estoque.listarAbaixoDoMinimo());
            case 7 -> relatorio();
            case 8 -> alterarRegimeTributario();
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
                lerInteiroNaoNegativo("Estoque mínimo: ")
        );

        estoque.cadastrar(produto);
        System.out.println("Produto cadastrado com sucesso.");
    }

    /** Registra entrada quando entrada é true; saída quando é false. */
    private static void movimentar(boolean entrada) {
        String codigo = lerTexto("Código: ");
        int quantidade = lerInteiro("Quantidade: ");

        if (entrada) {
            estoque.registrarEntrada(codigo, quantidade);
            System.out.println("Entrada registrada com sucesso.");
        } else {
            estoque.registrarSaida(codigo, quantidade);
            System.out.println("Saída registrada com sucesso.");
        }
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
                return RegimeTributario.porOpcao(lerInteiro("Escolha o regime: "));
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

        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Digite um número inteiro válido.");
        }
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
