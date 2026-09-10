/** Representa um cliente que pode receber produtos em uma saída de estoque. */
public final class Cliente extends Pessoa {
    private final String email;

    public Cliente(String codigo, String nome, String email) {
        super(codigo, nome, "CLI");
        this.email = validarEmail(email);
    }

    public String getEmail() {
        return email;
    }

    private static String validarEmail(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("E-mail obrigatório.");
        }

        String email = valor.trim();

        if (email.length() > 100 || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }

        return email;
    }
}
