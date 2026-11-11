package Los_Aromos.modelo;

public class Residente extends Persona {

    private String tipoResidente;

    public Residente(String dni, String nombre, String apellido, String tipoResidente) {
        super(dni, nombre, apellido);
        this.tipoResidente = tipoResidente;
    }

    @Override
    public String getRol() {
        return "Residente " + tipoResidente;
    }

    public String getTipoResidente() {
        return tipoResidente;
    }
}
