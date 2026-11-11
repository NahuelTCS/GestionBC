package Los_Aromos.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Expensa mensual de un lote.
public class Expensa {

    private String periodo;          // por ejemplo 2026-11
    private LocalDate fecha;         // fecha de emision
    private LocalDate vencimiento;   // dia 10 del periodo
    private double importe;          // capital original
    private double saldo;            // lo que falta pagar del capital
    private double interesesPendientes;
    private String estado;           // pendiente, parcial, pagada

    public Expensa(String periodo, LocalDate vencimiento, double importe) {
        this.periodo = periodo;
        this.fecha = LocalDate.now();
        this.vencimiento = vencimiento;
        this.importe = importe;
        this.saldo = importe;
        this.interesesPendientes = 0;
        this.estado = "pendiente";
    }

    // Regla de negocio: desde el vencimiento se cobra un 3 % mensual sobre el saldo impago.
    // Es interes simple: 3 % por cada mes o fraccion (supuesto S10 del analisis).
    public double calcularIntereses(LocalDate fechaActual) {
        if (estado.equals("pagada") || !fechaActual.isAfter(vencimiento)) {
            return 0;
        }
        long dias = ChronoUnit.DAYS.between(vencimiento, fechaActual);
        long meses = dias / 30;
        if (dias % 30 != 0) {
            meses = meses + 1;
        }
        double intereses = saldo * 0.03 * meses;
        this.interesesPendientes = intereses;
        return intereses;
    }

    // Aplica un importe a esta expensa: primero cancela los intereses y despues el capital.
    // Devuelve lo que sobro, para poder imputarlo a la expensa siguiente.
    public double aplicarImporte(double monto) {
        if (monto >= interesesPendientes) {
            monto = monto - interesesPendientes;
            interesesPendientes = 0;
        } else {
            interesesPendientes = interesesPendientes - monto;
            monto = 0;
        }

        if (monto >= saldo) {
            monto = monto - saldo;
            saldo = 0;
        } else {
            saldo = saldo - monto;
            monto = 0;
        }

        actualizarEstado();
        return monto;
    }

    // Lo usa el ajuste en contra, cuando se cobro de menos.
    public void sumarAlSaldo(double monto) {
        this.saldo = this.saldo + monto;
        actualizarEstado();
    }

    private void actualizarEstado() {
        if (saldo == 0 && interesesPendientes == 0) {
            estado = "pagada";
        } else if (saldo < importe) {
            estado = "parcial";
        } else {
            estado = "pendiente";
        }
    }

    public boolean estaImpaga() {
        return !estado.equals("pagada");
    }

    public boolean estaVencida(LocalDate fechaActual) {
        return estaImpaga() && fechaActual.isAfter(vencimiento);
    }

    public String getPeriodo() {
        return periodo;
    }

    public double getImporte() {
        return importe;
    }

    public double getSaldo() {
        return saldo;
    }

    public double getInteresesPendientes() {
        return interesesPendientes;
    }

    public LocalDate getVencimiento() {
        return vencimiento;
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return "Expensa " + periodo + " (emitida el " + fecha + ") | importe " + redondear(importe)
                + " | saldo " + redondear(saldo)
                + " | intereses " + redondear(interesesPendientes)
                + " | " + estado;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
