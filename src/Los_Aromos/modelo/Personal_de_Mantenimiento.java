package Los_Aromos.modelo;

// Empleado de mantenimiento. Lleva la cuenta de sus reclamos en curso.
public class Personal_de_Mantenimiento extends Personal {

    private String especialidad;
    private int reclamosEnCurso;

    public Personal_de_Mantenimiento(String dni, String nombre, String apellido, String legajo,
                                     String especialidad) {
        super(dni, nombre, apellido, legajo);
        this.especialidad = especialidad;
        this.reclamosEnCurso = 0;
    }

    @Override
    public String getRol() {
        return "Mantenimiento (" + especialidad + ")";
    }

    public int getReclamosEnCurso() {
        return reclamosEnCurso;
    }

    public void sumarReclamo() {
        reclamosEnCurso = reclamosEnCurso + 1;
    }

    public void restarReclamo() {
        if (reclamosEnCurso > 0) {
            reclamosEnCurso = reclamosEnCurso - 1;
        }
    }

    public String getEspecialidad() {
        return especialidad;
    }
}
