package Los_Aromos.pruebas;

import Los_Aromos.excepciones.PagoRechazadoException;
import Los_Aromos.modelo.*;
import java.time.LocalDate;

// Pruebas de las reglas de expensas, intereses y pagos.
public class PruebaExpensaYPago {

    public static void main(String[] args) {
        System.out.println("=== PRUEBAS DE EXPENSAS Y PAGOS ===");
        probarCalculoDeExpensa();
        probarIntereses();
        probarImputacionDelPago();
        probarPagoRechazado();
        probarMorosidad();
        Verificador.mostrarResumen("Pruebas de expensas y pagos");
    }

    private static void probarCalculoDeExpensa() {
        Titular titular = new Titular("30111222", "Ana", "Gomez");
        Lote lote = new Lote(101, 500, titular);

        // Presupuesto 1.000.000 y el lote ocupa 500 de 1000 m2, o sea el 50 %
        double expensa = lote.calcularExpensa(1000000, 1000);
        Verificador.verificar("La expensa es el 50 % del presupuesto", 500000.0, expensa);
    }

    private static void probarIntereses() {
        Expensa expensa = new Expensa("2026-09", LocalDate.of(2026, 9, 10), 100000);

        // Antes del vencimiento no hay intereses
        double sinMora = expensa.calcularIntereses(LocalDate.of(2026, 9, 5));
        Verificador.verificar("Antes del vencimiento no hay intereses", 0.0, sinMora);

        // Un mes despues del vencimiento: 3 % de 100000
        double unMes = expensa.calcularIntereses(LocalDate.of(2026, 10, 10));
        Verificador.verificar("A un mes de vencida son 3000 de interes", 3000.0, unMes);
    }

    private static void probarImputacionDelPago() {
        Expensa expensa = new Expensa("2026-09", LocalDate.of(2026, 9, 10), 100000);
        expensa.calcularIntereses(LocalDate.of(2026, 10, 10)); // quedan 3000 de intereses

        // Se paga 3000: tiene que cubrir primero los intereses y no tocar el capital
        expensa.aplicarImporte(3000);
        Verificador.verificar("El pago cubre primero los intereses", 0.0, expensa.getInteresesPendientes());
        Verificador.verificar("El capital queda intacto", 100000.0, expensa.getSaldo());
        Verificador.verificar("La expensa sigue pendiente", "pendiente", expensa.getEstado());

        // Ahora se paga la mitad del capital
        expensa.aplicarImporte(50000);
        Verificador.verificar("Queda la mitad del capital", 50000.0, expensa.getSaldo());
        Verificador.verificar("La expensa queda parcial", "parcial", expensa.getEstado());

        // Y por ultimo el resto
        double sobrante = expensa.aplicarImporte(60000);
        Verificador.verificar("La expensa queda pagada", "pagada", expensa.getEstado());
        Verificador.verificar("El sobrante se devuelve para la expensa siguiente", 10000.0, sobrante);
    }

    private static void probarPagoRechazado() {
        Expensa expensa = new Expensa("2026-10", LocalDate.of(2026, 10, 10), 600000);

        // La tarjeta tiene un limite de 500000, asi que este pago se rechaza
        MedioPago tarjeta = new PagoTarjeta("4000123412341234", 1, "Visa", 500000);
        Pago pago = new Pago(600000, tarjeta);
        try {
            pago.imputarPago(expensa);
            Verificador.verificar("Un pago sobre el limite se rechaza", "excepcion", "no la lanzo");
        } catch (PagoRechazadoException e) {
            Verificador.verificar("Un pago sobre el limite se rechaza", "excepcion", "excepcion");
        }
        Verificador.verificar("La expensa no se toco", 600000.0, expensa.getSaldo());

        // En efectivo no hay limite, pero hay que pagar el capital mas los intereses
        double intereses = expensa.calcularIntereses(LocalDate.now());
        double totalAPagar = expensa.getSaldo() + intereses;

        MedioPago efectivo = new PagoEfectivo("R-001");
        Pago pagoEfectivo = new Pago(totalAPagar, efectivo);
        try {
            pagoEfectivo.imputarPago(expensa);
            Verificador.verificar("En efectivo el pago se acepta", "pagada", expensa.getEstado());
        } catch (PagoRechazadoException e) {
            Verificador.verificar("En efectivo el pago se acepta", "pagada", "lo rechazo");
        }

        // Si solo se paga el capital y quedan intereses, la expensa no queda saldada
        Expensa otra = new Expensa("2026-05", LocalDate.of(2026, 5, 10), 100000);
        otra.calcularIntereses(LocalDate.now());
        otra.aplicarImporte(100000);
        Verificador.verificar("Pagando solo el capital la expensa queda parcial",
                "parcial", otra.getEstado());
    }

    private static void probarMorosidad() {
        Titular titular = new Titular("28333444", "Luis", "Perez");
        Lote lote = new Lote(102, 300, titular);

        // Tres expensas impagas todavia no alcanzan para ser moroso
        for (int i = 1; i <= 3; i++) {
            lote.agregarExpensa(new Expensa("2026-0" + i, LocalDate.of(2026, i, 10), 50000));
        }
        lote.verificarMorosidad();
        Verificador.verificar("Con 3 periodos impagos no es moroso", false, lote.isMoroso());

        // La cuarta si
        lote.agregarExpensa(new Expensa("2026-04", LocalDate.of(2026, 4, 10), 50000));
        lote.verificarMorosidad();
        Verificador.verificar("Con 4 periodos impagos queda moroso", true, lote.isMoroso());

        // Al pagar todo se le quita la condicion
        for (Expensa expensa : lote.getExpensas()) {
            expensa.calcularIntereses(LocalDate.now());
            expensa.aplicarImporte(expensa.getSaldo() + expensa.getInteresesPendientes());
        }
        lote.verificarMorosidad();
        Verificador.verificar("Al regularizar deja de ser moroso", false, lote.isMoroso());
    }
}
