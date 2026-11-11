package Los_Aromos.modelo;

// Asi se puede agregar un medio de pago nuevo sin tocar la clase Pago.
public interface MedioPago {

    boolean procesarPago(double importe);

    String getDescripcion();
}
