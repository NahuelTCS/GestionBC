package Los_Aromos.modelo;

import java.time.LocalDate;

// Orden de trabajo que se genera cuando un equipo tiene una falla.
public class Orden_Mantenimiento {

    private int numero;
    private LocalDate fechaApertura;
    private String prioridad;
    private String descripcionTrabajo;
    private LocalDate fechaCierre;
    private Equipo equipo;
    private Personal_de_Mantenimiento tecnico;

    public Orden_Mantenimiento(int numero, Equipo equipo, String prioridad) {
        this.numero = numero;
        this.equipo = equipo;
        this.prioridad = prioridad;
        this.fechaApertura = LocalDate.now();
        this.fechaCierre = null;
    }

    public void asignarTecnico(Personal_de_Mantenimiento tecnico) {
        this.tecnico = tecnico;
        equipo.ponerEnReparacion();
    }

    // Una orden no se puede cerrar sin registrar la reparacion y el tecnico.
    // Devuelve false si falta alguno de los dos datos.
    public boolean cerrarOrden(String descripcionTrabajo) {
        if (tecnico == null || descripcionTrabajo == null || descripcionTrabajo.isEmpty()) {
            return false;
        }
        this.descripcionTrabajo = descripcionTrabajo;
        this.fechaCierre = LocalDate.now();
        equipo.volverAOperativo();
        return true;
    }

    public boolean estaAbierta() {
        return fechaCierre == null;
    }

    public int getNumero() {
        return numero;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    @Override
    public String toString() {
        String estado;
        if (estaAbierta()) {
            estado = "abierta";
        } else {
            estado = "cerrada el " + fechaCierre;
        }
        return "Orden " + numero + " del " + fechaApertura + " | equipo " + equipo.getCodigo()
                + " | prioridad " + prioridad + " | " + estado;
    }
}
