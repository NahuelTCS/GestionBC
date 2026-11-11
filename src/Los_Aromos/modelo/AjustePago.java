package Los_Aromos.modelo;

// Regla de negocio: un pago registrado no se borra.
// Si hubo un error se corrige con un ajuste, que es un pago con motivo.
public class AjustePago extends Pago {

    private String motivoAjuste;
    private String tipoAjuste; // a favor o en contra

    public AjustePago(double importe, MedioPago medioPago, String motivoAjuste, String tipoAjuste) {
        super(importe, medioPago);
        this.motivoAjuste = motivoAjuste;
        this.tipoAjuste = tipoAjuste;
    }

    // Aplica el ajuste sobre la expensa. No borra el pago anterior:
    // si el ajuste es a favor del vecino le descuenta, y si es en contra le suma.
    public void aplicarAjuste(Expensa expensa) {
        if (tipoAjuste.equalsIgnoreCase("a favor")) {
            expensa.aplicarImporte(importe);
        } else {
            expensa.sumarAlSaldo(importe);
        }
        this.expensa = expensa;
    }

    public String getMotivoAjuste() {
        return motivoAjuste;
    }

    @Override
    public String toString() {
        return "Ajuste " + tipoAjuste + " del " + fecha + " por " + importe
                + ". Motivo: " + motivoAjuste;
    }
}
