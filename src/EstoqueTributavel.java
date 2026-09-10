import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/** Centraliza as regras de cadastro, movimentação e relatório do estoque. */
public final class EstoqueTributavel {
    private final Map<String, Produto> produtos = new LinkedHashMap<>();
    private final Map<String, Cliente> clientes = new LinkedHashMap<>();
    private final Map<String, Funcionario> funcionarios = new LinkedHashMap<>();
    private RegimeTributario regimeTributario;

    /** Cria o estoque já associado a um regime tributário. */
    public EstoqueTributavel(RegimeTributario regimeTributario) {
        definirRegimeTributario(regimeTributario);
    }

    public RegimeTributario getRegimeTributario() {
        return regimeTributario;
    }

    /** Permite simular a mesma loja em outro regime tributário. */
    public void definirRegimeTributario(RegimeTributario regimeTributario) {
        if (regimeTributario == null) {
            throw new IllegalArgumentException("Regime tributário obrigatório.");
        }

        this.regimeTributario = regimeTributario;
    }

    /** Cadastra um cliente apenas se ainda não existir outro com o mesmo código. */
    public void cadastrarCliente(Cliente cliente) {
        cadastrarPessoa(clientes, cliente, "cliente");
    }

    /** Cadastra um funcionário apenas se ainda não existir outro com o mesmo código. */
    public void cadastrarFuncionario(Funcionario funcionario) {
        cadastrarPessoa(funcionarios, funcionario, "funcionário");
    }

    /**
     * Cadastra o produto apenas se o funcionário responsável já estiver cadastrado
     * e não existir outro produto com o mesmo código.
     */
    public void cadastrarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto obrigatório.");
        }
        buscarFuncionario(produto.getCodigoFuncionarioResponsavel());

        if (produtos.putIfAbsent(produto.getCodigo(), produto) != null) {
            throw new IllegalArgumentException(
                    "Já existe um produto com o código " + produto.getCodigo() + "."
            );
        }
    }

    /** Procura o produto pelo código informado no terminal. */
    public Produto buscarPorCodigo(String codigo) {
        Produto produto = produtos.get(normalizarCodigo(codigo));

        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        return produto;
    }

    public Cliente buscarCliente(String codigo) {
        Cliente cliente = clientes.get(normalizarCodigo(codigo));

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        return cliente;
    }

    public Funcionario buscarFuncionario(String codigo) {
        Funcionario funcionario = funcionarios.get(normalizarCodigo(codigo));

        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionário não encontrado.");
        }

        return funcionario;
    }

    /** Registra a chegada de novas unidades de um produto. */
    public void registrarEntrada(String codigo, int quantidade, String codigoFuncionario) {
        buscarFuncionario(codigoFuncionario);
        Produto produto = buscarPorCodigo(codigo);
        produto.adicionarEstoque(quantidade);
    }

    /** Registra uma venda para um cliente já cadastrado. */
    public void registrarSaidaParaCliente(String codigo, int quantidade, String codigoCliente) {
        buscarCliente(codigoCliente);
        Produto produto = buscarPorCodigo(codigo);
        produto.retirarEstoque(quantidade);
    }

    /** Retorna uma cópia ordenada por código para proteger o Map interno. */
    public List<Produto> listarProdutos() {
        List<Produto> lista = new ArrayList<>(produtos.values());
        lista.sort(Comparator.comparing(Produto::getCodigo));
        return List.copyOf(lista);
    }

    public List<Cliente> listarClientes() {
        return listarPessoas(clientes);
    }

    public List<Funcionario> listarFuncionarios() {
        return listarPessoas(funcionarios);
    }

    /** Filtra somente os produtos que exigem atenção para reposição. */
    public List<Produto> listarAbaixoDoMinimo() {
        return listarProdutos()
                .stream()
                .filter(Produto::estaAbaixoDoMinimo)
                .toList();
    }

    public BigDecimal getValorTotalSemTributo() {
        return somar(Produto::getValorEstoqueSemTributo);
    }

    public BigDecimal getTributoTotalEstimado() {
        return regimeTributario.calcularTributo(getValorTotalSemTributo());
    }

    public BigDecimal getValorTotalComTributo() {
        return getValorTotalSemTributo().add(getTributoTotalEstimado());
    }

    /** Calcula o tributo estimado de um produto no regime selecionado. */
    public BigDecimal calcularTributoDoProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto obrigatório.");
        }

        return regimeTributario.calcularTributo(produto.getValorEstoqueSemTributo());
    }

    /** Aplica um cálculo a todos os produtos e devolve a soma final. */
    private BigDecimal somar(Function<Produto, BigDecimal> funcao) {
        return produtos.values()
                .stream()
                .map(funcao)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /** Evita a repetição da regra de código único entre clientes e funcionários. */
    private <T extends Pessoa> void cadastrarPessoa(Map<String, T> pessoas, T pessoa, String tipo) {
        if (pessoa == null) {
            throw new IllegalArgumentException("Cadastro obrigatório.");
        }

        if (pessoas.putIfAbsent(pessoa.getCodigo(), pessoa) != null) {
            throw new IllegalArgumentException(
                    "Já existe um " + tipo + " com o código " + pessoa.getCodigo() + "."
            );
        }
    }

    private static String normalizarCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código obrigatório.");
        }

        return codigo.trim().toUpperCase(Locale.ROOT);
    }

    private static <T extends Pessoa> List<T> listarPessoas(Map<String, T> pessoas) {
        return pessoas.values()
                .stream()
                .sorted(Comparator.comparing(Pessoa::getCodigo))
                .toList();
    }
}
