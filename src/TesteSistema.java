import java.math.BigDecimal;

/** Testes sem bibliotecas externas: execute antes da apresentação. */
public class TesteSistema {
    private static int aprovados = 0;

    public static void main(String[] args) {
        testarCadastroEMovimentacoes();
        testarBloqueiosDeEntradaInvalida();
        testarProtecaoContraEstoqueInsuficiente();
        testarCalculoTributavel();
        System.out.println("\nTodos os " + aprovados + " testes passaram.");
    }

    private static void testarCadastroEMovimentacoes() {
        EstoqueTributavel estoque = novoEstoqueComProduto();
        estoque.registrarEntrada("abc-101", 5);
        verificar(estoque.buscarPorCodigo("ABC-101").getQuantidadeEmEstoque() == 15, "Entrada deveria somar estoque");
        estoque.registrarSaida("ABC-101", 3);
        verificar(estoque.buscarPorCodigo("ABC-101").getQuantidadeEmEstoque() == 12, "Saída deveria subtrair estoque");
    }

    private static void testarBloqueiosDeEntradaInvalida() {
        esperarErro(() -> new Produto("x", "A", "B", new BigDecimal("10"), 0, 0, BigDecimal.ZERO), "código curto");
        esperarErro(() -> new Produto("ABC", "Arroz", "Alimento", new BigDecimal("10.999"), 0, 0, BigDecimal.ZERO), "preço com mais de duas casas");
        esperarErro(() -> new Produto("ABC", "Arroz", "Alimento", new BigDecimal("10"), -1, 0, BigDecimal.ZERO), "estoque inicial negativo");
        esperarErro(() -> new Produto("ABC", "Arroz", "Alimento", new BigDecimal("10"), 0, 0, new BigDecimal("100.01")), "alíquota acima de 100");

        EstoqueTributavel estoque = novoEstoqueComProduto();
        esperarErro(() -> estoque.cadastrar(new Produto("ABC-101", "Outro", "Teste", new BigDecimal("1"), 0, 0, BigDecimal.ZERO)), "código duplicado");
        esperarErro(() -> estoque.registrarEntrada("ABC-101", 0), "entrada zero");
        esperarErro(() -> estoque.buscarPorCodigo("NAO-EXISTE"), "produto inexistente");
    }

    private static void testarProtecaoContraEstoqueInsuficiente() {
        EstoqueTributavel estoque = novoEstoqueComProduto();
        esperarErro(() -> estoque.registrarSaida("ABC-101", 11), "saída maior que estoque");
        verificar(estoque.buscarPorCodigo("ABC-101").getQuantidadeEmEstoque() == 10,
                "Falha na saída não pode alterar o estoque");
        esperarErro(() -> estoque.registrarSaida("ABC-101", -5), "saída negativa");
    }

    private static void testarCalculoTributavel() {
        EstoqueTributavel estoque = novoEstoqueComProduto(); // 10 x R$ 20 = R$ 200; tributo 18% = R$ 36
        verificar(estoque.getValorTotalSemTributo().compareTo(new BigDecimal("200.00")) == 0, "valor sem tributo incorreto");
        verificar(estoque.getTributoTotalEstimado().compareTo(new BigDecimal("36.00")) == 0, "tributo incorreto");
        verificar(estoque.getValorTotalComTributo().compareTo(new BigDecimal("236.00")) == 0, "valor com tributo incorreto");
    }

    private static EstoqueTributavel novoEstoqueComProduto() {
        EstoqueTributavel estoque = new EstoqueTributavel();
        estoque.cadastrar(new Produto("ABC-101", "Arroz Tipo 1", "Alimentos", new BigDecimal("20.00"), 10, 3, new BigDecimal("18.00")));
        return estoque;
    }

    private static void verificar(boolean condicao, String mensagem) {
        if (!condicao) throw new AssertionError(mensagem);
        aprovados++;
    }
    private static void esperarErro(Runnable acao, String cenario) {
        try { acao.run(); }
        catch (IllegalArgumentException e) { aprovados++; return; }
        throw new AssertionError("O sistema deveria bloquear: " + cenario);
    }
}
