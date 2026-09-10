import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** Regras de negócio: cadastro, movimentação e consolidação de valores tributáveis. */
public final class EstoqueTributavel {
    private final Map<String, Produto> produtos = new LinkedHashMap<>();

    public void cadastrar(Produto produto) {
        Objects.requireNonNull(produto, "Produto obrigatório.");
        if (produtos.putIfAbsent(produto.getCodigo(), produto) != null) {
            throw new IllegalArgumentException("Já existe um produto com o código " + produto.getCodigo() + ".");
        }
    }

    public Produto buscarPorCodigo(String codigo) {
        if (codigo == null) throw new IllegalArgumentException("Código obrigatório.");
        Produto produto = produtos.get(codigo.trim().toUpperCase(Locale.ROOT));
        if (produto == null) throw new IllegalArgumentException("Produto não encontrado.");
        return produto;
    }

    public void registrarEntrada(String codigo, int quantidade) { buscarPorCodigo(codigo).adicionarEstoque(quantidade); }
    public void registrarSaida(String codigo, int quantidade) { buscarPorCodigo(codigo).retirarEstoque(quantidade); }

    public List<Produto> listarProdutos() {
        List<Produto> lista = new ArrayList<>(produtos.values());
        lista.sort(Comparator.comparing(Produto::getCodigo));
        return List.copyOf(lista);
    }

    public List<Produto> listarAbaixoDoMinimo() {
        return listarProdutos().stream().filter(Produto::estaAbaixoDoMinimo).toList();
    }

    public BigDecimal getValorTotalSemTributo() { return somar(Produto::getValorEstoqueSemTributo); }
    public BigDecimal getTributoTotalEstimado() { return somar(Produto::getValorTributoEstoque); }
    public BigDecimal getValorTotalComTributo() { return somar(Produto::getValorEstoqueComTributo); }

    private BigDecimal somar(java.util.function.Function<Produto, BigDecimal> funcao) {
        return produtos.values().stream().map(funcao).reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
