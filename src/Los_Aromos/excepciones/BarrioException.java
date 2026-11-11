package Los_Aromos.excepciones;

// Excepcion base de las reglas de negocio del barrio.
public class BarrioException extends Exception {

    public BarrioException(String mensaje) {
        super(mensaje);
    }
}
