package Los_Aromos.repositorios;

import Los_Aromos.modelo.Lote;
import Los_Aromos.modelo.Titular;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// Guarda los lotes en un archivo de texto.
// Cada linea tiene: numero;superficie;moroso;dniTitular;nombre;apellido
public class ArchivoRepositorioLotes implements RepositorioLotes {

    private String rutaArchivo;

    public ArchivoRepositorioLotes(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public void guardar(Lote lote) {
        // Si el lote ya estaba guardado se reescribe el archivo con el dato nuevo
        List<Lote> lotes = listarTodos();
        boolean encontrado = false;
        for (int i = 0; i < lotes.size(); i++) {
            if (lotes.get(i).getNumero() == lote.getNumero()) {
                lotes.set(i, lote);
                encontrado = true;
            }
        }
        if (!encontrado) {
            lotes.add(lote);
        }
        escribirTodos(lotes);
    }

    private void escribirTodos(List<Lote> lotes) {
        PrintWriter escritor = null;
        try {
            escritor = new PrintWriter(new FileWriter(rutaArchivo));
            for (Lote lote : lotes) {
                Titular titular = lote.getTitular();
                escritor.println(lote.getNumero() + ";" + lote.getSuperficie() + ";"
                        + lote.isMoroso() + ";" + titular.getDni() + ";"
                        + titular.getNombreCompleto());
            }
        } catch (IOException e) {
            System.out.println("No se pudo escribir el archivo de lotes: " + e.getMessage());
        } finally {
            if (escritor != null) {
                escritor.close();
            }
        }
    }

    @Override
    public Lote buscarPorNumero(int numero) {
        List<Lote> lotes = listarTodos();
        for (Lote lote : lotes) {
            if (lote.getNumero() == numero) {
                return lote;
            }
        }
        return null;
    }

    @Override
    public List<Lote> listarTodos() {
        List<Lote> lotes = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return lotes;
        }

        BufferedReader lector = null;
        try {
            lector = new BufferedReader(new FileReader(archivo));
            String linea = lector.readLine();
            while (linea != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 5) {
                    String[] nombreCompleto = datos[4].split(" ");
                    String nombre = nombreCompleto[0];
                    String apellido = "";
                    if (nombreCompleto.length > 1) {
                        apellido = nombreCompleto[1];
                    }
                    Titular titular = new Titular(datos[3], nombre, apellido);
                    Lote lote = new Lote(Integer.parseInt(datos[0]),
                            Double.parseDouble(datos[1]), titular);
                    lote.marcarMoroso(Boolean.parseBoolean(datos[2]));
                    lotes.add(lote);
                }
                linea = lector.readLine();
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de lotes: " + e.getMessage());
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
            } catch (IOException e) {
                System.out.println("No se pudo cerrar el archivo de lotes.");
            }
        }
        return lotes;
    }
}
