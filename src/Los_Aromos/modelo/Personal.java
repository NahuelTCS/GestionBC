package Los_Aromos.modelo;

import java.time.LocalDate;

public abstract class Personal extends Persona {

    protected String legajo;
    protected LocalDate fechaIngreso;

    public Personal(String dni, String nombre, String apellido, String legajo) {
        super(dni, nombre, apellido);
        this.legajo = legajo;
        this.fechaIngreso = LocalDate.now();
    }

    public String getLegajo() {
        return legajo;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }
}
