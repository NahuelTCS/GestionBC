package Los_Aromos.modelo;

public class Titular extends Persona {

    public Titular(String dni, String nombre, String apellido) {
        super(dni, nombre, apellido);
    }

    @Override
    public String getRol() {
        return "Titular";
    }
}
