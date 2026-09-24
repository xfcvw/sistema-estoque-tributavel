/** Representa um cliente identificado pelo CPF em uma saída de estoque. */
public final class Cliente extends Pessoa {
    private final String cpf;
    private final String email;

    public Cliente(String cpf, String nome, String email) {
        super(nome);
        this.cpf = validarCpf(cpf);
        this.email = validarEmail(email);
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getIdentificador() {
        return cpf;
    }

    /** Valida os 11 números do CPF e seus dígitos verificadores. */
    private static String validarCpf(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("CPF obrigatório.");
        }

        String texto = valor.trim();

        if (!texto.matches("[0-9]{11}")) {
            throw new IllegalArgumentException("CPF deve conter somente 11 números.");
        }

        String cpf = texto;

        if (cpf.matches("([0-9])\\1{10}")) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        int primeiroDigito = calcularDigito(cpf.substring(0, 9), 10);
        int segundoDigito = calcularDigito(cpf.substring(0, 9) + primeiroDigito, 11);

        if (cpf.charAt(9) != Character.forDigit(primeiroDigito, 10)
                || cpf.charAt(10) != Character.forDigit(segundoDigito, 10)) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        return cpf;
    }

    private static int calcularDigito(String numeros, int pesoInicial) {
        int soma = 0;

        for (int indice = 0; indice < numeros.length(); indice++) {
            soma += Character.getNumericValue(numeros.charAt(indice)) * (pesoInicial - indice);
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
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
