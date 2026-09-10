import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Representa um produto cadastrado no estoque.
 * Os dados de identificação e tributação não mudam após o cadastro.
 */
public final class Produto {
    private final String codigo;
    private final String nome;
    private final String categoria;
    private final BigDecimal precoUnitario;
    private final BigDecimal aliquotaTributo;
    private final int estoqueMinimo;
    private int quantidadeEmEstoque;

    public Produto(
            String codigo,
            String nome,
            String categoria,
            BigDecimal precoUnitario,
            int quantidadeInicial,
            int estoqueMinimo,
            BigDecimal aliquotaTributo
    ) {
        this.codigo = validarCodigo(codigo);
        this.nome = validarTexto(nome, "Nome");
        this.categoria = validarTexto(categoria, "Categoria");
        this.precoUnitario = validarPreco(precoUnitario);
        this.quantidadeEmEstoque = validarQuantidade(quantidadeInicial, "Quantidade inicial");
        this.estoqueMinimo = validarQuantidade(estoqueMinimo, "Estoque mínimo");
        this.aliquotaTributo = validarAliquota(aliquotaTributo);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public BigDecimal getAliquotaTributo() {
        return aliquotaTributo;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public int getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    /** Adiciona uma quantidade positiva ao estoque. */
    public void adicionarEstoque(int quantidade) {
        quantidade = validarQuantidadePositiva(quantidade);

        try {
            // addExact evita que uma soma muito grande ultrapasse o limite de int.
            quantidadeEmEstoque = Math.addExact(quantidadeEmEstoque, quantidade);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Quantidade em estoque ultrapassa o limite permitido.");
        }
    }

    /** Retira unidades apenas quando há quantidade suficiente no estoque. */
    public void retirarEstoque(int quantidade) {
        quantidade = validarQuantidadePositiva(quantidade);

        if (quantidade > quantidadeEmEstoque) {
            throw new IllegalArgumentException(
                    "Estoque insuficiente. Disponível: " + quantidadeEmEstoque + "."
            );
        }

        quantidadeEmEstoque -= quantidade;
    }

    /** Retorna true quando o estoque alcançou ou ficou abaixo do mínimo. */
    public boolean estaAbaixoDoMinimo() {
        return quantidadeEmEstoque <= estoqueMinimo;
    }

    /** Calcula o valor atual das unidades guardadas, sem incluir tributos. */
    public BigDecimal getValorEstoqueSemTributo() {
        BigDecimal valor = precoUnitario.multiply(BigDecimal.valueOf(quantidadeEmEstoque));
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    /** Calcula o valor estimado de tributo das unidades em estoque. */
    public BigDecimal getValorTributoEstoque() {
        return getValorEstoqueSemTributo()
                .multiply(aliquotaTributo)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /** Soma o valor dos produtos em estoque ao tributo estimado. */
    public BigDecimal getValorEstoqueComTributo() {
        return getValorEstoqueSemTributo().add(getValorTributoEstoque());
    }

    private static String validarCodigo(String valor) {
        String codigo = Objects.requireNonNull(valor, "Código obrigatório.")
                .trim()
                .toUpperCase();

        if (!codigo.matches("[A-Z0-9-]{3,20}")) {
            throw new IllegalArgumentException(
                    "Código deve ter 3 a 20 caracteres: letras, números ou hífen."
            );
        }

        return codigo;
    }

    private static String validarTexto(String valor, String campo) {
        String texto = Objects.requireNonNull(valor, campo + " obrigatório.").trim();

        if (texto.length() < 2 || texto.length() > 80) {
            throw new IllegalArgumentException(campo + " deve ter entre 2 e 80 caracteres.");
        }

        return texto;
    }

    private static BigDecimal validarPreco(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0 || valor.scale() > 2) {
            throw new IllegalArgumentException(
                    "Preço deve ser positivo e ter no máximo duas casas decimais."
            );
        }

        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal validarAliquota(BigDecimal valor) {
        if (valor == null
                || valor.compareTo(BigDecimal.ZERO) < 0
                || valor.compareTo(BigDecimal.valueOf(100)) > 0
                || valor.scale() > 2) {
            throw new IllegalArgumentException(
                    "Alíquota deve estar entre 0 e 100, com no máximo duas casas decimais."
            );
        }

        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private static int validarQuantidade(int quantidade, String campo) {
        if (quantidade < 0) {
            throw new IllegalArgumentException(campo + " não pode ser negativa.");
        }

        return quantidade;
    }

    private static int validarQuantidadePositiva(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }

        return quantidade;
    }
}
