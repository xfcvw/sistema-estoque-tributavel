import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Regimes disponíveis na simulação acadêmica do projeto.
 *
 * As alíquotas são estimativas usadas apenas para demonstrar a diferença entre
 * os regimes no sistema. Um cálculo fiscal real depende da atividade da empresa,
 * faturamento, estado e outras regras legais.
 */
public enum RegimeTributario {
    SIMPLES_NACIONAL(1, "Simples Nacional", new BigDecimal("6.00")),
    LUCRO_PRESUMIDO(2, "Lucro Presumido", new BigDecimal("11.33")),
    LUCRO_REAL(3, "Lucro Real", new BigDecimal("15.00"));

    private final int opcao;
    private final String descricao;
    private final BigDecimal aliquotaEstimada;

    RegimeTributario(int opcao, String descricao, BigDecimal aliquotaEstimada) {
        this.opcao = opcao;
        this.descricao = descricao;
        this.aliquotaEstimada = aliquotaEstimada;
    }

    public int getOpcao() {
        return opcao;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getAliquotaEstimada() {
        return aliquotaEstimada;
    }

    /** Calcula o tributo estimado para um valor de estoque. */
    public BigDecimal calcularTributo(BigDecimal valorBase) {
        if (valorBase == null || valorBase.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor base não pode ser negativo.");
        }

        return valorBase
                .multiply(aliquotaEstimada)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /** Converte a opção numérica exibida no menu para o regime correspondente. */
    public static RegimeTributario porOpcao(int opcao) {
        for (RegimeTributario regime : values()) {
            if (regime.opcao == opcao) {
                return regime;
            }
        }

        throw new IllegalArgumentException("Regime tributário inválido.");
    }
}
