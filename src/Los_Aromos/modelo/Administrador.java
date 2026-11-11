package Los_Aromos.modelo;

public class Administrador extends Personal {

    public Administrador(String dni, String nombre, String apellido, String legajo) {
        super(dni, nombre, apellido, legajo);
    }

    @Override
    public String getRol() {
        return "Administrador";
    }
}
