package Los_Aromos.modelo;

import java.time.LocalDate;

// Camaras y luminarias. Cada tipo decide con que prioridad se abre su orden.
public abstract class Equipo {

    protected String codigo;
    protected String sector;
    protected LocalDate fechaInstalacion;
    protected String estado; // operativo, con falla, en reparacion, fuera de servicio

    public Equipo(String codigo, String sector) {
        this.codigo = codigo;
        this.sector = sector;
        this.fechaInstalacion = LocalDate.now();
        this.estado = "operativo";
    }

    // Prioridad que lleva la orden de mantenimiento al fallar este equipo.
    // Se le pasa cuantos equipos del mismo sector estan con falla, porque
    // la luminaria lo necesita para decidir y la camara no.
    public abstract String prioridadDeOrden(int fallasDelSector);

    public void registrarFalla() {
        this.estado = "con falla";
    }

    public void ponerEnReparacion() {
        this.estado = "en reparacion";
    }

    public void volverAOperativo() {
        this.estado = "operativo";
    }

    public String getCodigo() {
        return codigo;
    }

    public String getSector() {
        return sector;
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return codigo + " | sector " + sector + " | " + estado
                + " | instalado el " + fechaInstalacion;
    }
}
