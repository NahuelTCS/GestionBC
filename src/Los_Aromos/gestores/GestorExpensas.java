package Los_Aromos.gestores;

import Los_Aromos.excepciones.PagoRechazadoException;
import Los_Aromos.modelo.AjustePago;
import Los_Aromos.modelo.Expensa;
import Los_Aromos.modelo.Lote;
import Los_Aromos.modelo.MedioPago;
import Los_Aromos.modelo.Pago;
import Los_Aromos.repositorios.RepositorioExpensas;
import Los_Aromos.repositorios.RepositorioLotes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Se encarga de emitir expensas, registrar pagos y controlar la morosidad.
// Usa las interfaces de repositorio, no las clases que escriben los archivos.
public class GestorExpensas {

    private RepositorioExpensas repositorioExpensas;
    private RepositorioLotes repositorioLotes;
    private List<Pago> pagos;

    public GestorExpensas(RepositorioExpensas repositorioExpensas, RepositorioLotes repositorioLotes) {
        this.repositorioExpensas = repositorioExpensas;
        this.repositorioLotes = repositorioLotes;
        this.pagos = new ArrayList<>();
    }

    // Emite la expensa del periodo para cada lote.
    // El vencimiento es el dia 10 del periodo.
    public void calcularExpensasMensuales(String periodo, double presupuestoDelMes, List<Lote> lotes) {
        double superficieTotal = 0;
        for (Lote lote : lotes) {
            superficieTotal = superficieTotal + lote.getSuperficie();
        }

        LocalDate vencimiento = armarVencimiento(periodo);

        for (Lote lote : lotes) {
            double importe = lote.calcularExpensa(presupuestoDelMes, superficieTotal);
            Expensa expensa = new Expensa(periodo, vencimiento, importe);
            lote.agregarExpensa(expensa);
            repositorioExpensas.guardar(expensa);
            lote.verificarMorosidad();
            if (lote.isMoroso()) {
                notificarMorosidad(lote);
            }
            repositorioLotes.guardar(lote);
        }
    }

    // CU16: se avisa al titular. Por ahora la notificacion se simula por consola.
    private void notificarMorosidad(Lote lote) {
        String contacto = lote.getTitular().getEmail();
        if (contacto == null) {
            contacto = lote.getTitular().getTelefono();
        }
        if (contacto == null) {
            contacto = "sin datos de contacto";
        }
        System.out.println("AVISO de morosidad al titular del lote " + lote.getNumero()
                + " (" + lote.getTitular().getNombreCompleto() + ") a " + contacto);
    }

    private LocalDate armarVencimiento(String periodo) {
        String[] partes = periodo.split("-");
        int anio = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        return LocalDate.of(anio, mes, 10);
    }

    // Calcula los intereses de todas las expensas vencidas de cada lote.
    public void aplicarInteresesPorMora(List<Lote> lotes) {
        LocalDate hoy = LocalDate.now();
        for (Lote lote : lotes) {
            for (Expensa expensa : lote.getExpensas()) {
                expensa.calcularIntereses(hoy);
            }
        }
    }

    // Registra un pago sobre un lote.
    // Imputa el importe empezando por la expensa mas vieja; lo que sobra pasa a la siguiente.
    // Si queda sin deuda, se le quita la condicion de moroso.
    public Pago registrarPago(Lote lote, double importe, MedioPago medioPago)
            throws PagoRechazadoException {

        Pago pago = new Pago(importe, medioPago);
        double restante = importe;

        for (Expensa expensa : lote.getExpensas()) {
            if (restante <= 0) {
                break;
            }
            if (expensa.estaImpaga()) {
                Pago parcial = new Pago(restante, medioPago);
                restante = parcial.imputarPago(expensa);
            }
        }

        lote.verificarMorosidad();
        repositorioLotes.guardar(lote);
        pagos.add(pago);
        return pago;
    }

    // Regla de negocio: un pago no se elimina. Si hubo un error se corrige
    // con un asiento de ajuste, que queda registrado junto a los pagos.
    public AjustePago registrarAjuste(Lote lote, Expensa expensa, double importe,
                                      MedioPago medioPago, String motivo, String tipo) {
        AjustePago ajuste = new AjustePago(importe, medioPago, motivo, tipo);
        ajuste.aplicarAjuste(expensa);
        lote.verificarMorosidad();
        repositorioLotes.guardar(lote);
        pagos.add(ajuste);
        return ajuste;
    }

    public List<Pago> getPagos() {
        return pagos;
    }
}
