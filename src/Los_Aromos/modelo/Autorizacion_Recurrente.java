package Los_Aromos.modelo;

import java.time.LocalDate;

// Autorizacion para un proveedor habitual, valida hasta 30 dias.
public class Autorizacion_Recurrente extends Autorizacion {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int diasVigencia;
    private String proveedorNombre;
    private String proveedorDni;

    public Autorizacion_Recurrente(String idAutorizacion, Titular titular, String franjaHoraria,
                                   String proveedorNombre, String proveedorDni, int diasVigencia) {
        super(idAutorizacion, titular, franjaHoraria);
        // Regla de negocio: la vigencia no puede pasar de 30 dias
        if (diasVigencia > 30) {
            diasVigencia = 30;
        }
        this.diasVigencia = diasVigencia;
        this.fechaInicio = LocalDate.now();
        this.fechaFin = fechaInicio.plusDays(diasVigencia);
        this.proveedorNombre = proveedorNombre;
        this.proveedorDni = proveedorDni;
    }

    @Override
    public boolean esValida(LocalDate fecha) {
        if (!estado.equals("vigente")) {
            return false;
        }
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }

    // La recurrente no se marca como usada: sirve hasta que vence.
    @Override
    public void marcarUsada() {
        // se deja vigente a proposito
    }

    public String getProveedorDni() {
        return proveedorDni;
    }

    @Override
    public String toString() {
        return "Autorizacion recurrente " + idAutorizacion + " para " + proveedorNombre
                + " (DNI " + proveedorDni + ") por " + diasVigencia + " dias, del "
                + fechaInicio + " al " + fechaFin;
    }
}
