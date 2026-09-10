import java.math.BigDecimal;

/**
 * Testes automatizados sem bibliotecas externas.
 * Esta classe tenta usar dados inválidos para confirmar as proteções do sistema.
 */
public class TesteSistema {
    private static int aprovados = 0;

    public static void main(String[] args) {
        testarCadastroEMovimentacoes();
        testarBloqueiosDeEntradaInvalida();
        testarProtecaoContraEstoqueInsuficiente();
        testarCalculoTributavel();
        testarTrocaDeRegimeTributario();

        System.out.println("\nTodos os " + aprovados + " testes passaram.");
    }

    private static void testarCadastroEMovimentacoes() {
        EstoqueTributavel estoque = novoEstoqueComProduto();

        // Entrada aumenta a quantidade disponível.
        estoque.registrarEntrada("abc-101", 5);
        verificar(
                estoque.buscarPorCodigo("ABC-101").getQuantidadeEmEstoque() == 15,
                "Entrada deveria somar estoque"
        );

        // Saída diminui a quantidade disponível.
        estoque.registrarSaida("ABC-101", 3);
        verificar(
                estoque.buscarPorCodigo("ABC-101").getQuantidadeEmEstoque() == 12,
                "Saída deveria subtrair estoque"
        );
    }

    private static void testarBloqueiosDeEntradaInvalida() {
        // Cada chamada abaixo usa um dado que a regra de negócio deve bloquear.
        esperarErro(() -> new Produto("x", "A", "B", new BigDecimal("10"), 0, 0), "código curto");
        esperarErro(() -> new Produto("ABC", "Arroz", "Alimento", new BigDecimal("10.999"), 0, 0), "preço com mais de duas casas");
        esperarErro(() -> new Produto("ABC", "Arroz", "Alimento", new BigDecimal("10"), -1, 0), "estoque inicial negativo");

        EstoqueTributavel estoque = novoEstoqueComProduto();
        esperarErro(() -> estoque.cadastrar(new Produto("ABC-101", "Outro", "Teste", new BigDecimal("1"), 0, 0)), "código duplicado");
        esperarErro(() -> estoque.registrarEntrada("ABC-101", 0), "entrada zero");
        esperarErro(() -> estoque.buscarPorCodigo("NAO-EXISTE"), "produto inexistente");
    }

    private static void testarProtecaoContraEstoqueInsuficiente() {
        EstoqueTributavel estoque = novoEstoqueComProduto();

        // A saída inválida deve falhar antes de alterar o estoque.
        esperarErro(() -> estoque.registrarSaida("ABC-101", 11), "saída maior que estoque");
        verificar(
                estoque.buscarPorCodigo("ABC-101").getQuantidadeEmEstoque() == 10,
                "Falha na saída não pode alterar o estoque"
        );
        esperarErro(() -> estoque.registrarSaida("ABC-101", -5), "saída negativa");
    }

    private static void testarCalculoTributavel() {
        EstoqueTributavel estoque = novoEstoqueComProduto();

        // 10 x R$ 20 = R$ 200; Simples Nacional estimado em 6% = R$ 12.
        verificar(estoque.getValorTotalSemTributo().compareTo(new BigDecimal("200.00")) == 0, "valor sem tributo incorreto");
        verificar(estoque.getTributoTotalEstimado().compareTo(new BigDecimal("12.00")) == 0, "tributo incorreto");
        verificar(estoque.getValorTotalComTributo().compareTo(new BigDecimal("212.00")) == 0, "valor com tributo incorreto");
    }

    private static void testarTrocaDeRegimeTributario() {
        EstoqueTributavel estoque = novoEstoqueComProduto();

        estoque.definirRegimeTributario(RegimeTributario.LUCRO_PRESUMIDO);
        verificar(
                estoque.getTributoTotalEstimado().compareTo(new BigDecimal("22.66")) == 0,
                "Lucro Presumido deveria usar a alíquota estimada de 11,33%"
        );

        estoque.definirRegimeTributario(RegimeTributario.LUCRO_REAL);
        verificar(
                estoque.getTributoTotalEstimado().compareTo(new BigDecimal("30.00")) == 0,
                "Lucro Real deveria usar a alíquota estimada de 15%"
        );

        esperarErro(() -> RegimeTributario.porOpcao(4), "opção de regime inexistente");
        esperarErro(() -> estoque.definirRegimeTributario(null), "regime nulo");
    }

    private static EstoqueTributavel novoEstoqueComProduto() {
        EstoqueTributavel estoque = new EstoqueTributavel(RegimeTributario.SIMPLES_NACIONAL);
        Produto produto = new Produto(
                "ABC-101",
                "Arroz Tipo 1",
                "Alimentos",
                new BigDecimal("20.00"),
                10,
                3
        );

        estoque.cadastrar(produto);
        return estoque;
    }

    private static void verificar(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new AssertionError(mensagem);
        }

        aprovados++;
    }

    private static void esperarErro(Runnable acao, String cenario) {
        try {
            acao.run();
        } catch (IllegalArgumentException e) {
            aprovados++;
            return;
        }

        throw new AssertionError("O sistema deveria bloquear: " + cenario);
    }
}
