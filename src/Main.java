import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Aplicação de terminal do Sistema de Gerenciamento de Estoque Tributável. */
public class Main {
    private static final Scanner ENTRADA = new Scanner(System.in);
    private static final EstoqueTributavel ESTOQUE = new EstoqueTributavel();
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public static void main(String[] args) {
        System.out.println("=== Sistema de Gerenciamento de Estoque Tributável ===");
        int opcao;
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
    }

    private static void mostrarMenu() {
        System.out.println("\n1 - Cadastrar produto\n2 - Registrar entrada\n3 - Registrar saída\n4 - Consultar produto\n5 - Listar estoque\n6 - Produtos no estoque mínimo\n7 - Relatório tributável\n0 - Sair");
    }

    private static void executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> cadastrarProduto();
            case 2 -> movimentar(true);
            case 3 -> movimentar(false);
            case 4 -> exibirProduto(ESTOQUE.buscarPorCodigo(lerTexto("Código: ")));
            case 5 -> listar(ESTOQUE.listarProdutos());
            case 6 -> listar(ESTOQUE.listarAbaixoDoMinimo());
            case 7 -> relatorio();
            case 0 -> { }
            default -> System.out.println("Opção inválida. Escolha uma opção do menu.");
        }
    }

    private static void cadastrarProduto() {
        Produto produto = new Produto(
                lerTexto("Código: "), lerTexto("Nome: "), lerTexto("Categoria: "),
                lerDecimal("Preço unitário (ex.: 19,90): "), lerInteiroNaoNegativo("Quantidade inicial: "),
                lerInteiroNaoNegativo("Estoque mínimo: "), lerDecimal("Alíquota tributária % (ex.: 18): "));
        ESTOQUE.cadastrar(produto);
        System.out.println("Produto cadastrado com sucesso.");
    }

    private static void movimentar(boolean entrada) {
        String codigo = lerTexto("Código: ");
        int quantidade = lerInteiro("Quantidade: ");
        if (entrada) ESTOQUE.registrarEntrada(codigo, quantidade);
        else ESTOQUE.registrarSaida(codigo, quantidade);
        System.out.println((entrada ? "Entrada" : "Saída") + " registrada com sucesso.");
    }

    private static void listar(List<Produto> produtos) {
        if (produtos.isEmpty()) { System.out.println("Nenhum produto encontrado."); return; }
        produtos.forEach(Main::exibirProduto);
    }

    private static void exibirProduto(Produto p) {
        System.out.printf("%n[%s] %s | Categoria: %s%nEstoque: %d (mínimo: %d)%nPreço: %s | Tributo: %s%%%nValor tributável no estoque: %s%n",
                p.getCodigo(), p.getNome(), p.getCategoria(), p.getQuantidadeEmEstoque(), p.getEstoqueMinimo(),
                MOEDA.format(p.getPrecoUnitario()), p.getAliquotaTributo().toPlainString(), MOEDA.format(p.getValorTributoEstoque()));
    }

    private static void relatorio() {
        System.out.println("\n--- Relatório de Estoque Tributável ---");
        System.out.println("Valor dos produtos: " + MOEDA.format(ESTOQUE.getValorTotalSemTributo()));
        System.out.println("Tributos estimados: " + MOEDA.format(ESTOQUE.getTributoTotalEstimado()));
        System.out.println("Valor total com tributos: " + MOEDA.format(ESTOQUE.getValorTotalComTributo()));
        System.out.println("Itens no mínimo/abaixo: " + ESTOQUE.listarAbaixoDoMinimo().size());
    }

    private static String lerTexto(String rotulo) { System.out.print(rotulo); return ENTRADA.nextLine(); }
    private static int lerInteiroNaoNegativo(String rotulo) {
        int valor = lerInteiro(rotulo);
        if (valor < 0) throw new IllegalArgumentException("O valor não pode ser negativo.");
        return valor;
    }
    private static int lerInteiro(String rotulo) {
        System.out.print(rotulo);
        String valor = ENTRADA.nextLine().trim();
        try { return Integer.parseInt(valor); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Digite um número inteiro válido."); }
    }
    private static BigDecimal lerDecimal(String rotulo) {
        System.out.print(rotulo);
        String valor = ENTRADA.nextLine().trim().replace(',', '.');
        try { return new BigDecimal(valor); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Digite um valor decimal válido."); }
    }
}
