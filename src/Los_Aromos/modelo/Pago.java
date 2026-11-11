package Los_Aromos.modelo;

import Los_Aromos.excepciones.PagoRechazadoException;
import java.time.LocalDate;

// Pago de una expensa.
// El pago no sabe como se cobra: eso lo resuelve el medio de pago.
public class Pago {

    protected LocalDate fecha;
    protected double importe;
    protected MedioPago medioPago;
    protected Expensa expensa;

    public Pago(double importe, MedioPago medioPago) {
        this.fecha = LocalDate.now();
        this.importe = importe;
        this.medioPago = medioPago;
    }

    // Imputa el pago sobre una expensa.
    // Primero pide al medio de pago que procese el importe; si lo rechaza, no se registra nada.
    // Despues calcula los intereses si esta vencida y aplica el importe.
    // Devuelve el sobrante, que el gestor imputa a la expensa siguiente.
    public double imputarPago(Expensa expensa) throws PagoRechazadoException {
        if (!medioPago.procesarPago(importe)) {
            throw new PagoRechazadoException("El medio de pago rechazo la operacion: "
                    + medioPago.getDescripcion());
        }

        this.expensa = expensa;
        expensa.calcularIntereses(LocalDate.now());
        double sobrante = expensa.aplicarImporte(importe);
        return sobrante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getImporte() {
        return importe;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    @Override
    public String toString() {
        String sobre = "";
        if (expensa != null) {
            sobre = " sobre la expensa " + expensa.getPeriodo();
        }
        return "Pago del " + fecha + " por " + importe
                + " con " + medioPago.getDescripcion() + sobre;
    }
}
