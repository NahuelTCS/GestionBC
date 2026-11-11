package Los_Aromos.gestores;

import Los_Aromos.modelo.Expensa;
import Los_Aromos.modelo.Lote;
import Los_Aromos.repositorios.RepositorioExpensas;
import Los_Aromos.repositorios.RepositorioLotes;
import java.time.LocalDate;
import java.util.List;

public class GestorExpensas {
    private RepositorioExpensas repositorioExpensas;
    private RepositorioLotes repositorioLotes;

    public GestorExpensas(RepositorioExpensas repoExpensas, RepositorioLotes repoLotes) {
        this.repositorioExpensas = repoExpensas;
        this.repositorioLotes = repoLotes;
    }

    // Regla de negocio: Expensa mensual = Presupuesto * Coeficiente (Superficie Lote / Superficie Total)
    public void calcularExpensasMensuales(String periodo, double presupuestoTotal, double superficieTotalBarrio, List<Lote> lotes) {
        System.out.println("\n--- Emitiendo Expensas para el período: " + periodo + " ---");
        LocalDate fechaEmision = LocalDate.now();
        LocalDate vencimiento = LocalDate.of(fechaEmision.getYear(), fechaEmision.getMonth(), 10); // Vence el 10

        for (Lote lote : lotes) {
            double coeficiente = lote.getSuperficie() / superficieTotalBarrio;
            double importeExpensa = presupuestoTotal * coeficiente;

            Expensa expensa = new Expensa(periodo, fechaEmision, vencimiento, importeExpensa);
            lote.agregarExpensa(expensa);
            repositorioExpensas.guardar(expensa);

            System.out.println("Lote " + lote.getNumero() + " | Importe emitido: $" + importeExpensa);
        }
    }

    public void aplicarInteresesPorMora(List<Lote> lotes) {
        System.out.println("\n--- Aplicando Intereses por Mora (3%) ---");
        LocalDate hoy = LocalDate.now();
        for (Lote lote : lotes) {
            for (Expensa exp : lote.getExpensas()) {
                double interes = exp.calcularIntereses(hoy);
                if (interes > 0) {
                    System.out.println("Lote " + lote.getNumero() + " (Período " + exp.getPeriodo() + "): Interés punitorio calculado = $" + interes);
                }
            }
        }
    }
}