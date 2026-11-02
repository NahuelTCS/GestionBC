package Los_Aromos.modelo;

import java.util.ArrayList;
import java.util.List;

public class Lote {
    private int numero;
    private double superficie;
    private boolean moroso;
    private List<Expensa> expensas; // Relación 1 a 1..* del UML

    public Lote(int numero, double superficie) {
        this.numero = numero;
        this.superficie = superficie;
        this.moroso = false;
        this.expensas = new ArrayList<>();
    }

    public void marcarMoroso() {
        this.moroso = true;
        System.out.println("Notificación formal: El lote " + numero + " supera los 3 períodos impagos y ha sido marcado como MOROSO.");
    }

    public void quitarMorosidad() {
        this.moroso = false;
        System.out.println("El lote " + numero + " ha regularizado su deuda y se retira la condición de moroso.");
    }

    public void agregarExpensa(Expensa expensa) {
        this.expensas.add(expensa);
        verificarMorosidad();
    }

    // Regla de negocio: Lote con más de 3 períodos impagos se marca como moroso
    public void verificarMorosidad() {
        long periodosImpagos = expensas.stream().filter(e -> e.isImpago()).count();
        if (periodosImpagos > 3) {
            marcarMoroso();
        } else if (moroso && periodosImpagos <= 3) {
            quitarMorosidad();
        }
    }

    public int getNumero() { return numero; }
    public double getSuperficie() { return superficie; }
    public boolean isMoroso() { return moroso; }
    public List<Expensa> getExpensas() { return expensas; }
}