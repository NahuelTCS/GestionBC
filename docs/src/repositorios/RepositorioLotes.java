package Los_Aromos.repositorios;

import Los_Aromos.modelo.Lote;
import java.util.List;

public interface RepositorioLotes {
    void guardar(Lote lote);
    Lote buscarPorId(int numero);
    List<Lote> listarTodos();
}