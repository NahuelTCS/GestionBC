package Los_Aromos.repositorios;

import Los_Aromos.modelo.Expensa;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Guarda las expensas en un archivo de texto.
// Cada linea tiene: periodo;vencimiento;importe;saldo;estado
public class ArchivoRepositorioExpensas implements RepositorioExpensas {

    private String rutaArchivo;

    public ArchivoRepositorioExpensas(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public void guardar(Expensa expensa) {
        PrintWriter escritor = null;
        try {
            // true para agregar al final y no borrar lo que ya estaba
            escritor = new PrintWriter(new FileWriter(rutaArchivo, true));
            escritor.println(expensa.getPeriodo() + ";" + expensa.getVencimiento() + ";"
                    + expensa.getImporte() + ";" + expensa.getSaldo() + ";"
                    + expensa.getEstado());
        } catch (IOException e) {
            System.out.println("No se pudo escribir el archivo de expensas: " + e.getMessage());
        } finally {
            if (escritor != null) {
                escritor.close();
            }
        }
    }

    @Override
    public List<Expensa> buscarPorPeriodo(String periodo) {
        List<Expensa> encontradas = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return encontradas;
        }

        BufferedReader lector = null;
        try {
            lector = new BufferedReader(new FileReader(archivo));
            String linea = lector.readLine();
            while (linea != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 5 && datos[0].equals(periodo)) {
                    Expensa expensa = new Expensa(datos[0], LocalDate.parse(datos[1]),
                            Double.parseDouble(datos[2]));
                    encontradas.add(expensa);
                }
                linea = lector.readLine();
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de expensas: " + e.getMessage());
        } finally {
            try {
                if (lector != null) {
                    lector.close();
                }
            } catch (IOException e) {
                System.out.println("No se pudo cerrar el archivo de expensas.");
            }
        }
        return encontradas;
    }
}
