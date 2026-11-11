package Los_Aromos.modelo;

import java.time.LocalDate;

public class CambioEstado {

    private LocalDate fecha;
    private String nuevoEstado;
    private String observacion;

    public CambioEstado(String nuevoEstado, String observacion) {
        this.fecha = LocalDate.now();
        this.nuevoEstado = nuevoEstado;
        this.observacion = observacion;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    @Override
    public String toString() {
        return fecha + " - paso a " + nuevoEstado + " (" + observacion + ")";
    }
}
