import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/** Centraliza as regras de cadastro, movimentação e relatório do estoque. */
public final class EstoqueTributavel {
    private final Map<String, Produto> produtos = new LinkedHashMap<>();

    /** Cadastra o produto apenas se ainda não existir outro com o mesmo código. */
    public void cadastrar(Produto produto) {
        Objects.requireNonNull(produto, "Produto obrigatório.");

        if (produtos.putIfAbsent(produto.getCodigo(), produto) != null) {
            throw new IllegalArgumentException(
                    "Já existe um produto com o código " + produto.getCodigo() + "."
            );
        }
    }

    /** Procura o produto pelo código informado no terminal. */
    public Produto buscarPorCodigo(String codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Código obrigatório.");
        }

        Produto produto = produtos.get(codigo.trim().toUpperCase(Locale.ROOT));

        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        return produto;
    }

    /** Registra a chegada de novas unidades de um produto. */
    public void registrarEntrada(String codigo, int quantidade) {
        Produto produto = buscarPorCodigo(codigo);
        produto.adicionarEstoque(quantidade);
    }

    /** Registra a retirada ou venda de unidades de um produto. */
    public void registrarSaida(String codigo, int quantidade) {
        Produto produto = buscarPorCodigo(codigo);
        produto.retirarEstoque(quantidade);
    }

    /** Retorna uma cópia ordenada por código para proteger o Map interno. */
    public List<Produto> listarProdutos() {
        List<Produto> lista = new ArrayList<>(produtos.values());
        lista.sort(Comparator.comparing(Produto::getCodigo));
        return List.copyOf(lista);
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
        return somar(Produto::getValorTributoEstoque);
    }

    public BigDecimal getValorTotalComTributo() {
        return somar(Produto::getValorEstoqueComTributo);
    }

    /** Aplica um cálculo a todos os produtos e devolve a soma final. */
    private BigDecimal somar(Function<Produto, BigDecimal> funcao) {
        return produtos.values()
                .stream()
                .map(funcao)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
