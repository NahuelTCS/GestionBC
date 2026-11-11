package Los_Aromos.repositorios;

import Los_Aromos.modelo.Lote;
import java.util.List;

// Los gestores trabajan contra esta interfaz y no contra los archivos.
public interface RepositorioLotes {

    void guardar(Lote lote);

    Lote buscarPorNumero(int numero);

    List<Lote> listarTodos();
}
