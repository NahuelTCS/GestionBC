package Los_Aromos.modelo;

import java.time.LocalDate;

// Autorizacion para una visita puntual, valida un solo dia.
public class Autorizacion_Unica extends Autorizacion {

    private String visitaNombre;
    private String visitaDNI;
    private LocalDate fechaValidez;

    public Autorizacion_Unica(String idAutorizacion, Titular titular, String franjaHoraria,
                              String visitaNombre, String visitaDNI, LocalDate fechaValidez) {
        super(idAutorizacion, titular, franjaHoraria);
        this.visitaNombre = visitaNombre;
        this.visitaDNI = visitaDNI;
        this.fechaValidez = fechaValidez;
    }

    @Override
    public boolean esValida(LocalDate fecha) {
        if (!estado.equals("vigente")) {
            return false;
        }
        return fecha.equals(fechaValidez);
    }

    public String getVisitaDNI() {
        return visitaDNI;
    }

    @Override
    public String toString() {
        return "Autorizacion unica " + idAutorizacion + " emitida el " + fechaEmision
                + " para " + visitaNombre + " el " + fechaValidez + " (" + franjaHoraria + ")";
    }
}
