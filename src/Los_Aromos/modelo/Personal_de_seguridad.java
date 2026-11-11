package Los_Aromos.modelo;

public class Personal_de_seguridad extends Personal {

    private String turno;
    private String puesto;

    public Personal_de_seguridad(String dni, String nombre, String apellido, String legajo,
                                 String turno, String puesto) {
        super(dni, nombre, apellido, legajo);
        this.turno = turno;
        this.puesto = puesto;
    }

    @Override
    public String getRol() {
        return "Seguridad (" + puesto + ", turno " + turno + ")";
    }
}
