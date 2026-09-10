import java.math.BigDecimal;
import java.util.List;

/**
 * Testes automatizados sem bibliotecas externas.
 * Cada cenário tenta violar uma regra para confirmar que o sistema se protege.
 */
public class TesteSistema {
    private static int aprovados = 0;

    public static void main(String[] args) {
        testarFluxoCompleto();
        testarValidacoesDeProduto();
        testarValidacoesDeClienteEFuncionario();
        testarBloqueiosDoEstoque();
        testarRegimesTributarios();
        testarColecoesProtegidas();

        System.out.println("\nTodos os " + aprovados + " testes passaram.");
    }

    private static void testarFluxoCompleto() {
        EstoqueTributavel estoque = novoEstoqueComCadastros();

        estoque.registrarEntrada("ABC-101", 5, "FUN-001");
        verificar(quantidadeDoArroz(estoque) == 15, "Entrada deveria aumentar o estoque");

        estoque.registrarSaidaParaCliente("ABC-101", 3, "CLI-001");
        verificar(quantidadeDoArroz(estoque) == 12, "Venda deveria diminuir o estoque");
        verificar(estoque.listarClientes().size() == 1, "Cliente deveria estar cadastrado");
        verificar(estoque.listarFuncionarios().size() == 1, "Funcionário deveria estar cadastrado");
        verificar(
                estoque.buscarPorCodigo("abc-101").getCodigoFuncionarioResponsavel().equals("FUN-001"),
                "Produto deveria guardar o funcionário responsável"
        );
    }

    private static void testarValidacoesDeProduto() {
        esperarErro(() -> new Produto(null, "Arroz", "Alimentos", new BigDecimal("10"), 0, 0, "FUN-001"), "código de produto nulo");
        esperarErro(() -> new Produto("ABC", null, "Alimentos", new BigDecimal("10"), 0, 0, "FUN-001"), "nome de produto nulo");
        esperarErro(() -> new Produto("ABC", "Arroz", null, new BigDecimal("10"), 0, 0, "FUN-001"), "categoria nula");
        esperarErro(() -> new Produto("ABC", "Arroz", "Alimentos", null, 0, 0, "FUN-001"), "preço nulo");
        esperarErro(() -> new Produto("ABC", "Arroz", "Alimentos", new BigDecimal("10"), 0, 0, null), "funcionário responsável nulo");
        esperarErro(() -> new Produto("x", "Arroz", "Alimentos", new BigDecimal("10"), 0, 0, "FUN-001"), "código de produto curto");
        esperarErro(() -> new Produto("ABC", "A", "Alimentos", new BigDecimal("10"), 0, 0, "FUN-001"), "nome curto");
        esperarErro(() -> new Produto("ABC", "Arroz", "A", new BigDecimal("10"), 0, 0, "FUN-001"), "categoria curta");
        esperarErro(() -> new Produto("ABC", "Arroz", "Alimentos", BigDecimal.ZERO, 0, 0, "FUN-001"), "preço zero");
        esperarErro(() -> new Produto("ABC", "Arroz", "Alimentos", new BigDecimal("10.999"), 0, 0, "FUN-001"), "preço com três casas");
        esperarErro(() -> new Produto("ABC", "Arroz", "Alimentos", new BigDecimal("10"), -1, 0, "FUN-001"), "estoque inicial negativo");
        esperarErro(() -> new Produto("ABC", "Arroz", "Alimentos", new BigDecimal("10"), 0, -1, "FUN-001"), "estoque mínimo negativo");
        esperarErro(() -> new Produto("ABC", "Arroz", "Alimentos", new BigDecimal("10"), 0, 0, "FUN-1"), "código de funcionário inválido no produto");

        Produto produtoNoLimite = new Produto(
                "LIM-001", "Produto Limite", "Teste", new BigDecimal("1"), Integer.MAX_VALUE, 0, "FUN-001"
        );
        esperarErro(() -> produtoNoLimite.adicionarEstoque(1), "estouro de quantidade");
    }

    private static void testarValidacoesDeClienteEFuncionario() {
        esperarErro(() -> new Cliente(null, "Cliente", "cliente@email.com"), "código de cliente nulo");
        esperarErro(() -> new Cliente("CLI-002", null, "cliente@email.com"), "nome de cliente nulo");
        esperarErro(() -> new Cliente("CLI-002", "Cliente", null), "e-mail nulo");
        esperarErro(() -> new Cliente("CLI-1", "Cliente", "cliente@email.com"), "código de cliente inválido");
        esperarErro(() -> new Cliente("CLI-002", "C", "cliente@email.com"), "nome de cliente curto");
        esperarErro(() -> new Cliente("CLI-002", "Cliente", "email-invalido"), "e-mail inválido");
        esperarErro(() -> new Funcionario(null, "Funcionário", "Caixa"), "código de funcionário nulo");
        esperarErro(() -> new Funcionario("FUN-002", null, "Caixa"), "nome de funcionário nulo");
        esperarErro(() -> new Funcionario("FUN-002", "Funcionário", null), "cargo nulo");
        esperarErro(() -> new Funcionario("FUN-1", "Funcionário", "Caixa"), "código de funcionário inválido");
        esperarErro(() -> new Funcionario("FUN-002", "F", "Caixa"), "nome de funcionário curto");
        esperarErro(() -> new Funcionario("FUN-002", "Funcionário", "C"), "cargo curto");

        EstoqueTributavel estoque = novoEstoqueComCadastros();
        esperarErro(() -> estoque.cadastrarCliente(new Cliente("CLI-001", "Outro", "outro@email.com")), "cliente duplicado");
        esperarErro(() -> estoque.cadastrarFuncionario(new Funcionario("FUN-001", "Outro", "Caixa")), "funcionário duplicado");
        esperarErro(() -> estoque.cadastrarCliente(null), "cliente nulo");
        esperarErro(() -> estoque.cadastrarFuncionario(null), "funcionário nulo");
    }

    private static void testarBloqueiosDoEstoque() {
        EstoqueTributavel estoque = novoEstoqueComCadastros();

        esperarErro(() -> estoque.cadastrarProduto(null), "produto nulo");
        esperarErro(() -> estoque.cadastrarProduto(criarProduto("SEM-001", "FUN-999")), "produto com funcionário inexistente");
        esperarErro(() -> estoque.cadastrarProduto(criarProduto("ABC-101", "FUN-001")), "produto duplicado");
        esperarErro(() -> estoque.buscarPorCodigo(null), "código nulo na busca de produto");
        esperarErro(() -> estoque.calcularTributoDoProduto(null), "produto nulo no cálculo tributário");
        esperarErro(() -> estoque.buscarCliente("CLI-999"), "cliente inexistente");
        esperarErro(() -> estoque.buscarFuncionario("FUN-999"), "funcionário inexistente");
        esperarErro(() -> estoque.registrarEntrada("ABC-101", 0, "FUN-001"), "entrada zero");
        esperarErro(() -> estoque.registrarEntrada("ABC-101", -1, "FUN-001"), "entrada negativa");
        esperarErro(() -> estoque.registrarEntrada("ABC-101", 1, "FUN-999"), "entrada por funcionário inexistente");
        esperarErro(() -> estoque.registrarEntrada("NAO-EXISTE", 1, "FUN-001"), "entrada para produto inexistente");
        esperarErro(() -> estoque.registrarSaidaParaCliente("ABC-101", 0, "CLI-001"), "saída zero");
        esperarErro(() -> estoque.registrarSaidaParaCliente("ABC-101", -1, "CLI-001"), "saída negativa");
        esperarErro(() -> estoque.registrarSaidaParaCliente("ABC-101", 1, "CLI-999"), "saída para cliente inexistente");
        esperarErro(() -> estoque.registrarSaidaParaCliente("NAO-EXISTE", 1, "CLI-001"), "saída de produto inexistente");
        esperarErro(() -> estoque.registrarSaidaParaCliente("ABC-101", 11, "CLI-001"), "saída maior que estoque");
        verificar(quantidadeDoArroz(estoque) == 10, "Uma operação rejeitada não pode alterar o estoque");
    }

    private static void testarRegimesTributarios() {
        EstoqueTributavel estoque = novoEstoqueComCadastros();

        verificar(estoque.getValorTotalSemTributo().compareTo(new BigDecimal("200.00")) == 0, "valor do estoque incorreto");
        verificar(estoque.getTributoTotalEstimado().compareTo(new BigDecimal("12.00")) == 0, "Simples Nacional incorreto");

        estoque.definirRegimeTributario(RegimeTributario.LUCRO_PRESUMIDO);
        verificar(estoque.getTributoTotalEstimado().compareTo(new BigDecimal("22.66")) == 0, "Lucro Presumido incorreto");

        estoque.definirRegimeTributario(RegimeTributario.LUCRO_REAL);
        verificar(estoque.getTributoTotalEstimado().compareTo(new BigDecimal("30.00")) == 0, "Lucro Real incorreto");
        esperarErro(() -> estoque.definirRegimeTributario(null), "regime nulo");
        esperarErro(() -> RegimeTributario.porOpcao(0), "opção de regime zero");
        esperarErro(() -> RegimeTributario.porOpcao(4), "opção de regime inexistente");
        esperarErro(() -> RegimeTributario.SIMPLES_NACIONAL.calcularTributo(new BigDecimal("-1")), "valor tributável negativo");
    }

    private static void testarColecoesProtegidas() {
        EstoqueTributavel estoque = novoEstoqueComCadastros();
        List<Produto> produtos = estoque.listarProdutos();
        List<Cliente> clientes = estoque.listarClientes();

        esperarErroNaoSuportado(() -> produtos.add(criarProduto("NOV-001", "FUN-001")), "lista de produtos imutável");
        esperarErroNaoSuportado(() -> clientes.clear(), "lista de clientes imutável");
    }

    private static EstoqueTributavel novoEstoqueComCadastros() {
        EstoqueTributavel estoque = new EstoqueTributavel(RegimeTributario.SIMPLES_NACIONAL);
        estoque.cadastrarFuncionario(new Funcionario("FUN-001", "Ana Souza", "Estoquista"));
        estoque.cadastrarCliente(new Cliente("CLI-001", "João Lima", "joao@email.com"));
        estoque.cadastrarProduto(criarProduto("ABC-101", "FUN-001"));
        return estoque;
    }

    private static Produto criarProduto(String codigo, String codigoFuncionario) {
        return new Produto(
                codigo,
                "Arroz Tipo 1",
                "Alimentos",
                new BigDecimal("20.00"),
                10,
                3,
                codigoFuncionario
        );
    }

    private static int quantidadeDoArroz(EstoqueTributavel estoque) {
        return estoque.buscarPorCodigo("ABC-101").getQuantidadeEmEstoque();
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

    private static void esperarErroNaoSuportado(Runnable acao, String cenario) {
        try {
            acao.run();
        } catch (UnsupportedOperationException e) {
            aprovados++;
            return;
        }

        throw new AssertionError("A coleção deveria ser protegida: " + cenario);
    }
}
