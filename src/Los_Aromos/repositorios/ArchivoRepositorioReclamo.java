package Los_Aromos.repositorios;

import Los_Aromos.modelo.Lote;
import Los_Aromos.modelo.Reclamo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// Guarda los reclamos en un archivo de texto.
// Cada linea tiene: numero;fecha;tipo;descripcion;prioridad;estado;numeroLote
// Recibe la lista de lotes para poder volver a enlazar cada reclamo con el suyo.
public class ArchivoRepositorioReclamo implements RepositorioReclamo {

    private String rutaArchivo;
    private List<Lote> lotes;

    public ArchivoRepositorioReclamo(String rutaArchivo, List<Lote> lotes) {
        this.rutaArchivo = rutaArchivo;
        this.lotes = lotes;
    }

    @Override
    public void guardar(Reclamo reclamo) {
        List<Reclamo> reclamos = leerTodos();
        boolean encontrado = false;
        for (int i = 0; i < reclamos.size(); i++) {
            if (reclamos.get(i).getNumero() == reclamo.getNumero()) {
                reclamos.set(i, reclamo);
                encontrado = true;
            }
        }
        if (!encontrado) {
            reclamos.add(reclamo);
        }
        escribirTodos(reclamos);
    }

    private void escribirTodos(List<Reclamo> reclamos) {
        PrintWriter escritor = null;
        try {
            escritor = new PrintWriter(new FileWriter(rutaArchivo));
            for (Reclamo reclamo : reclamos) {
                escritor.println(reclamo.getNumero() + ";" + reclamo.getFecha() + ";"
                        + reclamo.getTipo() + ";" + reclamo.getDescripcion() + ";"
                        + reclamo.getPrioridad() + ";" + reclamo.getEstado() + ";"
                        + reclamo.getLote().getNumero());
            }
        } catch (IOException e) {
            System.out.println("No se pudo escribir el archivo de reclamos: " + e.getMessage());
        } finally {
            if (escritor != null) {
                escritor.close();
            }
        }
    }

    @Override
    public Reclamo buscarPorNumero(int numero) {
        List<Reclamo> reclamos = leerTodos();
        for (Reclamo reclamo : reclamos) {
            if (reclamo.getNumero() == numero) {
                return reclamo;
            }
        }
        return null;
    }

    @Override
    public List<Reclamo> listarPendientes() {
        List<Reclamo> pendientes = new ArrayList<>();
        for (Reclamo reclamo : leerTodos()) {
            if (!reclamo.getEstado().equals("cerrado")) {
                pendientes.add(reclamo);
            }
        }
        return pendientes;
    }

    private List<Reclamo> leerTodos() {
        List<Reclamo> reclamos = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return reclamos;
        }

        BufferedReader lector = null;
        try {
            lector = new BufferedReader(new FileReader(archivo));
            String linea = lector.readLine();
            while (linea != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 7) {
                    Lote lote = buscarLote(Integer.parseInt(datos[6]));
                    if (lote != null) {
                        Reclamo reclamo = new Reclamo(Integer.parseInt(datos[0]), datos[2],
                                datos[3], lote, lote.getTitular());
                        reclamos.add(reclamo);
                    }
                }
                linea = lector.readLine();
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de reclamos: " + e.getMessage());
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
            } catch (IOException e) {
                System.out.println("No se pudo cerrar el archivo de reclamos.");
            }
        }
        return reclamos;
    }

    private Lote buscarLote(int numero) {
        for (Lote lote : lotes) {
            if (lote.getNumero() == numero) {
                return lote;
            }
        }
        return null;
    }
}
