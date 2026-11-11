package Los_Aromos.modelo;

public class PagoTransferencia implements MedioPago {

    private String cbuOrigen;
    private String banco;

    public PagoTransferencia(String cbuOrigen, String banco) {
        this.cbuOrigen = cbuOrigen;
        this.banco = banco;
    }

    @Override
    public boolean procesarPago(double importe) {
        // El CBU argentino tiene 22 digitos
        return importe > 0 && cbuOrigen.length() == 22;
    }

    @Override
    public String getDescripcion() {
        return "Transferencia desde " + banco;
    }
}
