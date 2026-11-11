package Los_Aromos.comparadores;

import Los_Aromos.modelo.Lote;
import java.util.Comparator;

public class ComparadorLotePorDeuda implements Comparator<Lote> {

    @Override
    public int compare(Lote uno, Lote otro) {
        // De mayor a menor deuda
        if (uno.getDeudaTotal() > otro.getDeudaTotal()) {
            return -1;
        }
        if (uno.getDeudaTotal() < otro.getDeudaTotal()) {
            return 1;
        }
        return 0;
    }
}
