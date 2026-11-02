package Los_Aromos;

import Los_Aromos.modelo.Lote;
import Los_Aromos.repositorios.*;
import Los_Aromos.gestores.GestorExpensas;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   SISTEMA DE GESTIÓN - BARRIO CERRADO LOS AROMOS ");
        System.out.println("==================================================");

        // 1. Inicializar Repositorios de Archivo
        RepositorioLotes repoLotes = new ArchivoRepositorioLotes("lotes.dat");
        RepositorioExpensas repoExpensas = new ArchivoRepositorioExpensas("expensas.dat");

        // 2. Instanciar el Gestor de Expensas (Patrón Controller con inyección de repositorios)
        GestorExpensas gestorExpensas = new GestorExpensas(repoExpensas, repoLotes);

        // 3. Simular datos del barrio (Ej: Superficien total y lista de lotes)
        List<Lote> lotesBarrio = new ArrayList<>();
        Lote lote1 = new Lote(101, 500.0);
        Lote lote2 = new Lote(102, 300.0);
        Lote lote3 = new Lote(103, 200.0);

        lotesBarrio.add(lote1);
        lotesBarrio.add(lote2);
        lotesBarrio.add(lote3);

        double superficieTotalBarrio = 1000.0; // Suma de superficies de prueba
        double presupuestoMensual = 2000000.0; // Presupuesto total del mes

        // 4. Ejecutar Casos de Uso según las Reglas de Negocio
        gestorExpensas.calcularExpensasMensuales("2026-11", presupuestoMensual, superficieTotalBarrio, lotesBarrio);
        gestorExpensas.aplicarInteresesPorMora(lotesBarrio);

        System.out.println("\n==================================================");
        System.out.println("           FIN DE LA PRUEBA DE SISTEMA            ");
        System.out.println("==================================================");
    }
}