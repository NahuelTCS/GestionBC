package Los_Aromos.modelo;

import java.time.LocalDate;

public class Pago {
    private LocalDate fecha;
    private double importe;

    public Pago(LocalDate fecha, double importe) {
        this.fecha = fecha;
        this.importe = importe;
    }

    // Regla de negocio: Los pagos parciales se imputan primero a los intereses y después al capital,
    // empezando por el período más antiguo. Un pago no se elimina, se corrige con un asiento de ajuste.
    public void imputarPago(double montoPago, Expensa expensaAntigua, double interesesPendientes) {
        System.out.println("Procesando pago de $" + montoPago);
        if (montoPago <= interesesPendientes) {
            System.out.println("-> El pago cubre parcialmente o en su totalidad los intereses.");
        } else {
            double remanenteCapital = montoPago - interesesPendientes;
            System.out.println("-> Intereses cubiertos. Remanente aplicado al capital de la expensa: $" + remanenteCapital);
        }
    }

    public LocalDate getFecha() { return fecha; }
    public double getImporte() { return importe; }
}