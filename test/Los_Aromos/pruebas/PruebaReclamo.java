package Los_Aromos.pruebas;

import Los_Aromos.excepciones.BarrioException;
import Los_Aromos.excepciones.LimiteReclamosException;
import Los_Aromos.excepciones.TransicionInvalidaException;
import Los_Aromos.gestores.GestorReclamo;
import Los_Aromos.modelo.*;
import Los_Aromos.repositorios.ArchivoRepositorioReclamo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Pruebas de las reglas de negocio de los reclamos.
public class PruebaReclamo {

    public static void main(String[] args) {
        System.out.println("=== PRUEBAS DE RECLAMO ===");
        probarPrioridad();
        probarEstados();
        probarLimiteDeCincoReclamos();
        probarReapertura();
        probarOrdenPorPrioridad();
        Verificador.mostrarResumen("Pruebas de reclamo");
    }

    private static Lote loteDePrueba() {
        Titular titular = new Titular("30111222", "Ana", "Gomez");
        return new Lote(101, 500, titular);
    }

    private static void probarPrioridad() {
        Lote lote = loteDePrueba();
        Reclamo seguridad = new Reclamo(1, "seguridad", "Porton roto", lote, lote.getTitular());
        Reclamo mantenimiento = new Reclamo(2, "mantenimiento", "Perdida de agua", lote, lote.getTitular());
        Reclamo convivencia = new Reclamo(3, "convivencia", "Ruidos molestos", lote, lote.getTitular());

        Verificador.verificar("Un reclamo de seguridad es prioridad alta", "alta", seguridad.getPrioridad());
        Verificador.verificar("Uno de mantenimiento es prioridad media", "media", mantenimiento.getPrioridad());
        Verificador.verificar("Uno de convivencia es prioridad baja", "baja", convivencia.getPrioridad());
    }

    private static void probarEstados() {
        Lote lote = loteDePrueba();
        Reclamo reclamo = new Reclamo(1, "mantenimiento", "Perdida de agua", lote, lote.getTitular());

        Verificador.verificar("Un reclamo nuevo arranca abierto", "abierto", reclamo.getEstado());

        try {
            reclamo.cambiarEstado("asignado", "Asignado a un tecnico");
            Verificador.verificar("Pasa de abierto a asignado", "asignado", reclamo.getEstado());
        } catch (TransicionInvalidaException e) {
            Verificador.verificar("Pasa de abierto a asignado", "asignado", "lanzo excepcion");
        }

        // No se puede saltear estados: de asignado no se puede ir a cerrado
        try {
            reclamo.cambiarEstado("cerrado", "Intento invalido");
            Verificador.verificar("Saltear estados lanza excepcion", "excepcion", "no la lanzo");
        } catch (TransicionInvalidaException e) {
            Verificador.verificar("Saltear estados lanza excepcion", "excepcion", "excepcion");
        }

        Verificador.verificar("El estado no cambio tras el intento invalido", "asignado", reclamo.getEstado());
        Verificador.verificar("Quedaron registrados los cambios de estado", 1, reclamo.getCambios().size());
    }

    private static void probarLimiteDeCincoReclamos() {
        List<Lote> lotes = new ArrayList<>();
        Lote lote = loteDePrueba();
        lotes.add(lote);

        GestorReclamo gestor = new GestorReclamo(
                new ArchivoRepositorioReclamo("prueba_reclamos.txt", lotes));
        Personal_de_Mantenimiento empleado =
                new Personal_de_Mantenimiento("27999000", "Carlos", "Ruiz", "L-01", "electricidad");

        // Se le asignan 5 reclamos, que es el maximo permitido
        for (int i = 1; i <= 5; i++) {
            Reclamo reclamo = new Reclamo(i, "mantenimiento", "Reclamo " + i, lote, lote.getTitular());
            gestor.registrarReclamo(reclamo);
            try {
                gestor.asignarReclamoAEmpleado(reclamo, empleado);
            } catch (BarrioException e) {
                Verificador.verificar("Asignar el reclamo " + i, "sin error", e.getMessage());
            }
        }
        Verificador.verificar("El empleado quedo con 5 reclamos en curso", 5, empleado.getReclamosEnCurso());

        // El sexto tiene que ser rechazado
        Reclamo sexto = new Reclamo(6, "mantenimiento", "Reclamo 6", lote, lote.getTitular());
        gestor.registrarReclamo(sexto);
        try {
            gestor.asignarReclamoAEmpleado(sexto, empleado);
            Verificador.verificar("El sexto reclamo se rechaza", "excepcion", "no la lanzo");
        } catch (LimiteReclamosException e) {
            Verificador.verificar("El sexto reclamo se rechaza", "excepcion", "excepcion");
        } catch (TransicionInvalidaException e) {
            Verificador.verificar("El sexto reclamo se rechaza", "excepcion", "transicion invalida");
        }

        Verificador.verificar("El sexto reclamo quedo abierto", "abierto", sexto.getEstado());
    }

    private static void probarReapertura() {
        Lote lote = loteDePrueba();
        Reclamo reclamo = new Reclamo(1, "mantenimiento", "Perdida de agua", lote, lote.getTitular());
        try {
            reclamo.cambiarEstado("asignado", "Asignado");
            reclamo.cambiarEstado("en curso", "El tecnico empezo");
            reclamo.cambiarEstado("resuelto", "Reparado");
        } catch (TransicionInvalidaException e) {
            System.out.println("Error preparando la prueba: " + e.getMessage());
        }

        // Dentro de los 5 dias se puede reabrir y vuelve a "en curso"
        boolean reabierto = reclamo.reabrir(LocalDate.now().plusDays(3));
        Verificador.verificar("Se reabre dentro de los 5 dias", true, reabierto);
        Verificador.verificar("Al reabrirse vuelve a en curso", "en curso", reclamo.getEstado());
    }

    private static void probarReaperturaFueraDePlazo() {
        Lote lote = loteDePrueba();
        Reclamo reclamo = new Reclamo(2, "mantenimiento", "Otra perdida", lote, lote.getTitular());
        try {
            reclamo.cambiarEstado("asignado", "Asignado");
            reclamo.cambiarEstado("en curso", "En curso");
            reclamo.cambiarEstado("resuelto", "Reparado");
        } catch (TransicionInvalidaException e) {
            System.out.println("Error preparando la prueba: " + e.getMessage());
        }
        boolean reabierto = reclamo.reabrir(LocalDate.now().plusDays(10));
        Verificador.verificar("No se reabre pasados los 5 dias", false, reabierto);
        Verificador.verificar("Pasado el plazo queda cerrado", "cerrado", reclamo.getEstado());
    }

    private static void probarOrdenPorPrioridad() {
        probarReaperturaFueraDePlazo();

        Lote lote = loteDePrueba();
        List<Reclamo> reclamos = new ArrayList<>();
        reclamos.add(new Reclamo(1, "convivencia", "Ruidos", lote, lote.getTitular()));
        reclamos.add(new Reclamo(2, "seguridad", "Porton roto", lote, lote.getTitular()));
        reclamos.add(new Reclamo(3, "mantenimiento", "Perdida", lote, lote.getTitular()));

        // Collections.sort usa el orden natural, que definimos en Comparable
        Collections.sort(reclamos);

        Verificador.verificar("Primero queda el de prioridad alta", "alta", reclamos.get(0).getPrioridad());
        Verificador.verificar("Segundo el de prioridad media", "media", reclamos.get(1).getPrioridad());
        Verificador.verificar("Ultimo el de prioridad baja", "baja", reclamos.get(2).getPrioridad());
    }
}
