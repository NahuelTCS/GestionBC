package Los_Aromos.modelo;

public class PagoTarjeta implements MedioPago {

    private String nroTarjeta;
    private int cuotas;
    private String tipoTarjeta;
    private double limite;

    public PagoTarjeta(String nroTarjeta, int cuotas, String tipoTarjeta, double limite) {
        this.nroTarjeta = nroTarjeta;
        this.cuotas = cuotas;
        this.tipoTarjeta = tipoTarjeta;
        this.limite = limite;
    }

    @Override
    public boolean procesarPago(double importe) {
        // Si el importe supera el limite de la tarjeta, la operacion se rechaza
        return importe > 0 && importe <= limite;
    }

    @Override
    public String getDescripcion() {
        return tipoTarjeta + " terminada en " + ultimosCuatro() + " en " + cuotas + " cuota/s";
    }

    private String ultimosCuatro() {
        if (nroTarjeta.length() < 4) {
            return nroTarjeta;
        }
        return nroTarjeta.substring(nroTarjeta.length() - 4);
    }
}
