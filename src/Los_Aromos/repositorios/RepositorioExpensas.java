package Los_Aromos.repositorios;

import Los_Aromos.modelo.Expensa;
import java.util.List;

public interface RepositorioExpensas {

    void guardar(Expensa expensa);

    List<Expensa> buscarPorPeriodo(String periodo);
}
