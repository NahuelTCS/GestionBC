package Los_Aromos.modelo;

import java.time.LocalDate;

// Base de las autorizaciones de ingreso. Cada subclase define cuando es valida.
public abstract class Autorizacion {

    protected String idAutorizacion;
    protected LocalDate fechaEmision;
    protected String franjaHoraria;
    protected String estado; // vigente, usada, vencida
    protected Titular titular;

    public Autorizacion(String idAutorizacion, Titular titular, String franjaHoraria) {
        this.idAutorizacion = idAutorizacion;
        this.titular = titular;
        this.franjaHoraria = franjaHoraria;
        this.fechaEmision = LocalDate.now();
        this.estado = "vigente";
    }

    // Cada tipo de autorizacion revisa sus propias fechas.
    public abstract boolean esValida(LocalDate fecha);

    public void marcarUsada() {
        this.estado = "usada";
    }

    public String getIdAutorizacion() {
        return idAutorizacion;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public String getFranjaHoraria() {
        return franjaHoraria;
    }
}
