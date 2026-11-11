package Los_Aromos.repositorios;

import Los_Aromos.modelo.Reclamo;
import java.util.List;

public interface RepositorioReclamo {

    void guardar(Reclamo reclamo);

    Reclamo buscarPorNumero(int numero);

    List<Reclamo> listarPendientes();
}
