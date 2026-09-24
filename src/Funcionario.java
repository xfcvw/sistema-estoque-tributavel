/** Representa o funcionário responsável pelos cadastros e entradas de produtos. */
public final class Funcionario extends Pessoa {
    private final String codigo;
    private final String cargo;

    public Funcionario(String codigo, String nome, String cargo) {
        super(nome);
        this.codigo = validarCodigo(codigo, "FUN");
        this.cargo = validarCargo(cargo);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCargo() {
        return cargo;
    }

    @Override
    public String getIdentificador() {
        return codigo;
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
