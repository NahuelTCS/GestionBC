package Los_Aromos.modelo;

import java.util.ArrayList;
import java.util.List;

// Lote del barrio. Tiene un titular, residentes y sus expensas.
public class Lote {

    private int numero;
    private double superficie;
    private boolean moroso;
    private Titular titular;
    private List<Residente> residentes;
    private List<Expensa> expensas;

    public Lote(int numero, double superficie, Titular titular) {
        this.numero = numero;
        this.superficie = superficie;
        this.titular = titular;
        this.moroso = false;
        this.residentes = new ArrayList<>();
        this.expensas = new ArrayList<>();
    }

    // Regla de negocio: la expensa del lote es el presupuesto del mes
    // por el coeficiente de participacion, que sale de la superficie.
    public double calcularExpensa(double presupuestoDelMes, double superficieTotalDelBarrio) {
        double coeficiente = superficie / superficieTotalDelBarrio;
        return presupuestoDelMes * coeficiente;
    }

    public void marcarMoroso(boolean valor) {
        this.moroso = valor;
    }

    public void agregarExpensa(Expensa expensa) {
        expensas.add(expensa);
    }

    public void agregarResidente(Residente residente) {
        residentes.add(residente);
    }

    // Regla de negocio: con mas de 3 periodos impagos el lote queda moroso.
    // La condicion se quita al regularizar la deuda.
    public void verificarMorosidad() {
        int impagas = 0;
        for (Expensa expensa : expensas) {
            if (expensa.estaImpaga()) {
                impagas = impagas + 1;
            }
        }
        marcarMoroso(impagas > 3);
    }

    public double getDeudaTotal() {
        double total = 0;
        for (Expensa expensa : expensas) {
            total = total + expensa.getSaldo() + expensa.getInteresesPendientes();
        }
        return total;
    }

    // Solo el titular o un residente registrado puede presentar reclamos (regla de negocio).
    public boolean puedeReclamar(Persona persona) {
        if (persona == titular) {
            return true;
        }
        for (Residente residente : residentes) {
            if (residente == persona) {
                return true;
            }
        }
        return false;
    }

    public int getNumero() {
        return numero;
    }

    public double getSuperficie() {
        return superficie;
    }

    public boolean isMoroso() {
        return moroso;
    }

    public Titular getTitular() {
        return titular;
    }

    public List<Expensa> getExpensas() {
        return expensas;
    }

    public List<Residente> getResidentes() {
        return residentes;
    }

    @Override
    public String toString() {
        String condicion;
        if (moroso) {
            condicion = "MOROSO";
        } else {
            condicion = "al dia";
        }
        return "Lote " + numero + " | " + superficie + " m2 | titular "
                + titular.getNombreCompleto() + " | " + condicion;
    }
}
