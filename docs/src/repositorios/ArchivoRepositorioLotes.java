package Los_Aromos.repositorios;

import Los_Aromos.modelo.Lote;
import java.util.ArrayList;
import java.util.List;

public class ArchivoRepositorioLotes implements RepositorioLotes {
    private String rutaArchivo;

    public ArchivoRepositorioLotes(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public void guardar(Lote lote) {
        System.out.println("[ArchivoRepositorioLotes] Guardando lote " + lote.getNumero() + " en " + rutaArchivo);
    }

    @Override
    public Lote buscarPorId(int numero) {
        // Simulación de búsqueda en persistencia de archivos
        return null;
    }

    @Override
    public List<Lote> listarTodos() {
        return new ArrayList<>();
    }
}