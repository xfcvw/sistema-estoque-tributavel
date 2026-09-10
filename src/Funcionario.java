/** Representa o funcionário responsável pelos cadastros e entradas de produtos. */
public final class Funcionario extends Pessoa {
    private final String cargo;

    public Funcionario(String codigo, String nome, String cargo) {
        super(codigo, nome, "FUN");
        this.cargo = validarCargo(cargo);
    }

    public String getCargo() {
        return cargo;
    }

    private static String validarCargo(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Cargo obrigatório.");
        }

        String cargo = valor.trim();

        if (cargo.length() < 2 || cargo.length() > 60) {
            throw new IllegalArgumentException("Cargo deve ter entre 2 e 60 caracteres.");
        }

        return cargo;
    }
}
