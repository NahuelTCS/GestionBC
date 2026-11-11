package Los_Aromos.modelo;

import java.time.LocalDate;

public class Expensa {
    private String periodo;
    private LocalDate fecha; // Fecha de emisión
    private LocalDate vencimiento;
    private double importe;
    private boolean pagada;

    public Expensa(String periodo, LocalDate fecha, LocalDate vencimiento, double importe) {
        this.periodo = periodo;
        this.fecha = fecha;
        this.vencimiento = vencimiento;
        this.importe = importe;
        this.pagada = false;
    }

    // Regla de negocio: 3% mensual sobre el saldo impago desde el vencimiento (día 10)
    public double calcularIntereses(LocalDate fechaActual) {
        if (!pagada && fechaActual.isAfter(vencimiento)) {
            return this.importe * 0.03;
        }
        return 0.0;
    }

    public boolean isImpago() {
        return !pagada && LocalDate.now().isAfter(vencimiento);
    }

    public void setPagada(boolean pagada) { this.pagada = pagada; }
    public String getPeriodo() { return periodo; }
    public double getImporte() { return importe; }
    public LocalDate getVencimiento() { return vencimiento; }
}