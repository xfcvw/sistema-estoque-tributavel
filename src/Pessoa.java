import java.util.Locale;

/** Classe base para os cadastros de clientes e funcionários. */
public abstract class Pessoa {
    private final String codigo;
    private final String nome;

    protected Pessoa(String codigo, String nome, String prefixo) {
        this.codigo = validarCodigo(codigo, prefixo);
        this.nome = validarNome(nome);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    /** Valida códigos no formato, por exemplo, CLI-001 ou FUN-001. */
    protected static String validarCodigo(String valor, String prefixo) {
        if (valor == null) {
            throw new IllegalArgumentException("Código obrigatório.");
        }

        String codigo = valor.trim().toUpperCase(Locale.ROOT);

        if (!codigo.matches(prefixo + "-[0-9]{3,6}")) {
            throw new IllegalArgumentException(
                    "Código deve seguir o formato " + prefixo + "-001 até " + prefixo + "-999999."
            );
        }

        return codigo;
    }

    protected static String validarNome(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Nome obrigatório.");
        }

        String nome = valor.trim();

        if (nome.length() < 2 || nome.length() > 80) {
            throw new IllegalArgumentException("Nome deve ter entre 2 e 80 caracteres.");
        }

        return nome;
    }
}
