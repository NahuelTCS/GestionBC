package Los_Aromos.comparadores;

import Los_Aromos.modelo.Reclamo;
import java.util.Comparator;

// El orden natural de Reclamo es por prioridad; este es por antiguedad.
public class ComparadorReclamoPorFecha implements Comparator<Reclamo> {

    @Override
    public int compare(Reclamo uno, Reclamo otro) {
        return uno.getFecha().compareTo(otro.getFecha());
    }
}
