package Los_Aromos.repositorios;

import Los_Aromos.modelo.Expensa;
import java.util.ArrayList;
import java.util.List;

public class ArchivoRepositorioExpensas implements RepositorioExpensas {
    private String rutaArchivo;

    public ArchivoRepositorioExpensas(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public void guardar(Expensa expensa) {
        System.out.println("[ArchivoRepositorioExpensas] Guardando expensa del período " + expensa.getPeriodo());
    }

    @Override
    public List<Expensa> buscarPorPeriodo(String periodo) {
        return new ArrayList<>();
    }
}