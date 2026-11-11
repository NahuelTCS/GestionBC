package Los_Aromos.modelo;

public class PagoEfectivo implements MedioPago {

    private String recibo;

    public PagoEfectivo(String recibo) {
        this.recibo = recibo;
    }

    @Override
    public boolean procesarPago(double importe) {
        // El efectivo se recibe en mano, siempre queda aprobado
        return importe > 0;
    }

    @Override
    public String getDescripcion() {
        return "Efectivo (recibo " + recibo + ")";
    }
}
