package Los_Aromos;

import Los_Aromos.excepciones.BarrioException;
import Los_Aromos.gestores.GestorAccesos;
import Los_Aromos.gestores.GestorExpensas;
import Los_Aromos.gestores.GestorMantenimiento;
import Los_Aromos.gestores.GestorReclamo;
import Los_Aromos.gestores.GeneradorReportes;
import Los_Aromos.comparadores.ComparadorReclamoPorFecha;
import Los_Aromos.modelo.*;
import Los_Aromos.repositorios.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

// Menu por consola del sistema del barrio cerrado Los Aromos.
public class Main {

    private static Scanner teclado = new Scanner(System.in);

    private static List<Lote> lotes = new ArrayList<>();
    private static List<Personal_de_Mantenimiento> empleados = new ArrayList<>();
    private static Personal_de_seguridad guardia;

    private static GestorReclamo gestorReclamo;
    private static GestorExpensas gestorExpensas;
    private static GestorMantenimiento gestorMantenimiento;
    private static GestorAccesos gestorAccesos;
    private static GeneradorReportes generadorReportes = new GeneradorReportes();

    private static int proximoNumeroDeReclamo = 1;
    private static int numeroDeRecibo = 0;

    public static void main(String[] args) {
        prepararSistema();
        cargarDatosDePrueba();

        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1: menuReclamos(); break;
                case 2: menuExpensas(); break;
                case 3: menuEquipos(); break;
                case 4: menuAccesos(); break;
                case 5: menuReportes(); break;
                case 0: System.out.println("Hasta luego."); break;
                default: System.out.println("Opcion invalida.");
            }
        }
    }

    private static void prepararSistema() {
        RepositorioLotes repoLotes = new ArchivoRepositorioLotes("lotes.txt");
        RepositorioExpensas repoExpensas = new ArchivoRepositorioExpensas("expensas.txt");
        RepositorioReclamo repoReclamos = new ArchivoRepositorioReclamo("reclamos.txt", lotes);

        gestorExpensas = new GestorExpensas(repoExpensas, repoLotes);
        gestorReclamo = new GestorReclamo(repoReclamos);
        gestorMantenimiento = new GestorMantenimiento();
        gestorAccesos = new GestorAccesos();
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("===== BARRIO CERRADO LOS AROMOS =====");
        System.out.println("1 - Reclamos");
        System.out.println("2 - Expensas y pagos");
        System.out.println("3 - Equipos y mantenimiento");
        System.out.println("4 - Visitas");
        System.out.println("5 - Reportes");
        System.out.println("0 - Salir");
    }

    // ---------- RECLAMOS ----------

    private static void menuReclamos() {
        System.out.println();
        System.out.println("--- RECLAMOS ---");
        System.out.println("1 - Presentar reclamo");
        System.out.println("2 - Asignar reclamo a un empleado");
        System.out.println("3 - Cambiar el estado de un reclamo");
        System.out.println("4 - Reabrir un reclamo");
        System.out.println("5 - Ver un reclamo");
        System.out.println("6 - Escalar reclamos vencidos");
        System.out.println("7 - Listar los reclamos por antiguedad");

        int opcion = leerEntero("Opcion: ");
        try {
            switch (opcion) {
                case 1: presentarReclamo(); break;
                case 2: asignarReclamo(); break;
                case 3: cambiarEstadoDeReclamo(); break;
                case 4: reabrirReclamo(); break;
                case 5: verReclamo(); break;
                case 6: escalarReclamos(); break;
                case 7: listarReclamosPorFecha(); break;
                default: System.out.println("Opcion invalida.");
            }
        } catch (BarrioException e) {
            System.out.println("No se pudo hacer la operacion: " + e.getMessage());
        }
    }

    private static void presentarReclamo() {
        int numeroLote = leerEntero("Numero de lote: ");
        Lote lote = buscarLote(numeroLote);
        if (lote == null) {
            System.out.println("No existe ese lote.");
            return;
        }
        String tipo = leerTexto("Tipo (seguridad / mantenimiento / convivencia / administrativo): ");
        String descripcion = leerTexto("Descripcion: ");

        Reclamo reclamo = new Reclamo(proximoNumeroDeReclamo, tipo, descripcion,
                lote, lote.getTitular());
        proximoNumeroDeReclamo = proximoNumeroDeReclamo + 1;
        gestorReclamo.registrarReclamo(reclamo);

        System.out.println("Reclamo " + reclamo.getNumero() + " registrado con prioridad "
                + reclamo.getPrioridad() + ".");
    }

    private static void asignarReclamo() throws BarrioException {
        int numero = leerEntero("Numero de reclamo: ");
        Reclamo reclamo = gestorReclamo.buscarPorNumero(numero);
        if (reclamo == null) {
            System.out.println("No existe ese reclamo.");
            return;
        }

        System.out.println("Empleados de mantenimiento:");
        for (int i = 0; i < empleados.size(); i++) {
            Personal_de_Mantenimiento empleado = empleados.get(i);
            System.out.println("  " + i + " - " + empleado.getNombreCompleto()
                    + " | legajo " + empleado.getLegajo()
                    + " | desde " + empleado.getFechaIngreso()
                    + " | " + empleado.getReclamosEnCurso() + " reclamos en curso");
        }
        int indice = leerEntero("Empleado: ");
        if (indice < 0 || indice >= empleados.size()) {
            System.out.println("Empleado invalido.");
            return;
        }

        gestorReclamo.asignarReclamoAEmpleado(reclamo, empleados.get(indice));
        System.out.println("Reclamo asignado.");
    }

    private static void cambiarEstadoDeReclamo() throws BarrioException {
        int numero = leerEntero("Numero de reclamo: ");
        Reclamo reclamo = gestorReclamo.buscarPorNumero(numero);
        if (reclamo == null) {
            System.out.println("No existe ese reclamo.");
            return;
        }
        System.out.println("Estado actual: " + reclamo.getEstado());
        String nuevo = leerTexto("Nuevo estado: ");
        String observacion = leerTexto("Observacion: ");
        reclamo.cambiarEstado(nuevo, observacion);
        System.out.println("El reclamo paso a " + reclamo.getEstado() + ".");
    }

    private static void reabrirReclamo() {
        int numero = leerEntero("Numero de reclamo: ");
        Reclamo reclamo = gestorReclamo.buscarPorNumero(numero);
        if (reclamo == null) {
            System.out.println("No existe ese reclamo.");
            return;
        }
        if (reclamo.reabrir(LocalDate.now())) {
            System.out.println("Reclamo reabierto. Estado: " + reclamo.getEstado());
        } else {
            System.out.println("No se pudo reabrir. Estado: " + reclamo.getEstado());
        }
    }

    private static void verReclamo() {
        int numero = leerEntero("Numero de reclamo: ");
        Reclamo reclamo = gestorReclamo.buscarPorNumero(numero);
        if (reclamo == null) {
            System.out.println("No existe ese reclamo.");
            return;
        }
        System.out.println(reclamo);
        System.out.println("Presentado por: " + reclamo.getAutor());
        System.out.println("Historial:");
        for (CambioEstado cambio : reclamo.getCambios()) {
            System.out.println("  " + cambio);
        }
        System.out.println("Comentarios:");
        for (Comentario comentario : reclamo.getComentarios()) {
            System.out.println("  " + comentario);
        }
    }

    private static void escalarReclamos() {
        List<Reclamo> escalados = gestorReclamo.escalarReclamosVencidos();
        if (escalados.isEmpty()) {
            System.out.println("No hay reclamos para escalar.");
            return;
        }
        for (Reclamo reclamo : escalados) {
            System.out.println("Escalado: " + reclamo);
        }
    }

    private static void listarReclamosPorFecha() {
        List<Reclamo> reclamos = new ArrayList<>(gestorReclamo.getReclamos());
        if (reclamos.isEmpty()) {
            System.out.println("Todavia no hay reclamos.");
            return;
        }
        // Orden personalizado: del mas viejo al mas nuevo
        Collections.sort(reclamos, new ComparadorReclamoPorFecha());
        for (Reclamo reclamo : reclamos) {
            System.out.println("   " + reclamo.getFecha() + " | " + reclamo);
        }
    }

    // ---------- EXPENSAS ----------

    private static void menuExpensas() {
        System.out.println();
        System.out.println("--- EXPENSAS Y PAGOS ---");
        System.out.println("1 - Emitir expensas del mes");
        System.out.println("2 - Registrar un pago");
        System.out.println("3 - Ver la cuenta de un lote");
        System.out.println("4 - Registrar un ajuste sobre una expensa");

        int opcion = leerEntero("Opcion: ");
        try {
            switch (opcion) {
                case 1: emitirExpensas(); break;
                case 2: registrarPago(); break;
                case 3: verCuentaDelLote(); break;
                case 4: registrarAjuste(); break;
                default: System.out.println("Opcion invalida.");
            }
        } catch (BarrioException e) {
            System.out.println("No se pudo hacer la operacion: " + e.getMessage());
        }
    }

    private static void emitirExpensas() {
        String periodo = leerTexto("Periodo (por ejemplo 2026-11): ");
        double presupuesto = leerDecimal("Presupuesto del mes: ");
        gestorExpensas.calcularExpensasMensuales(periodo, presupuesto, lotes);
        gestorExpensas.aplicarInteresesPorMora(lotes);

        for (Lote lote : lotes) {
            System.out.println(lote);
            for (Expensa expensa : lote.getExpensas()) {
                System.out.println("   " + expensa);
            }
        }
    }

    private static void registrarPago() throws BarrioException {
        int numeroLote = leerEntero("Numero de lote: ");
        Lote lote = buscarLote(numeroLote);
        if (lote == null) {
            System.out.println("No existe ese lote.");
            return;
        }
        double importe = leerDecimal("Importe a pagar: ");

        MedioPago medioPago = elegirMedioPago();
        Pago pago = gestorExpensas.registrarPago(lote, importe, medioPago);
        System.out.println(pago);
        verEstadoDeCuenta(lote);
    }

    private static void registrarAjuste() {
        int numeroLote = leerEntero("Numero de lote: ");
        Lote lote = buscarLote(numeroLote);
        if (lote == null) {
            System.out.println("No existe ese lote.");
            return;
        }
        if (lote.getExpensas().isEmpty()) {
            System.out.println("Ese lote todavia no tiene expensas.");
            return;
        }

        for (int i = 0; i < lote.getExpensas().size(); i++) {
            System.out.println("  " + i + " - " + lote.getExpensas().get(i));
        }
        int indice = leerEntero("Expensa a ajustar: ");
        if (indice < 0 || indice >= lote.getExpensas().size()) {
            System.out.println("Expensa invalida.");
            return;
        }

        String tipo = leerTexto("Tipo de ajuste (a favor / en contra): ");
        String motivo = leerTexto("Motivo: ");
        if (motivo.isEmpty()) {
            System.out.println("El ajuste necesita un motivo. No se registro nada.");
            return;
        }
        double importe = leerDecimal("Importe del ajuste: ");

        AjustePago ajuste = gestorExpensas.registrarAjuste(lote,
                lote.getExpensas().get(indice), importe, elegirMedioPago(), motivo, tipo);
        System.out.println(ajuste);
        verEstadoDeCuenta(lote);
    }

    private static void verCuentaDelLote() {
        int numeroLote = leerEntero("Numero de lote: ");
        Lote lote = buscarLote(numeroLote);
        if (lote == null) {
            System.out.println("No existe ese lote.");
            return;
        }
        verEstadoDeCuenta(lote);
    }

    private static MedioPago elegirMedioPago() {
        System.out.println("1 - Efectivo");
        System.out.println("2 - Tarjeta");
        System.out.println("3 - Transferencia");
        int forma = leerEntero("Medio de pago: ");

        if (forma == 2) {
            return new PagoTarjeta("4000123412341234", 1, "Visa", 500000);
        }
        if (forma == 3) {
            return new PagoTransferencia("0170099220000067797915", "Galicia");
        }
        numeroDeRecibo = numeroDeRecibo + 1;
        return new PagoEfectivo("R-" + numeroDeRecibo);
    }

    private static void verEstadoDeCuenta(Lote lote) {
        System.out.println(lote);
        for (Expensa expensa : lote.getExpensas()) {
            System.out.println("   " + expensa);
        }
        System.out.println("   Deuda total: " + redondear(lote.getDeudaTotal()));
    }

    // ---------- EQUIPOS ----------

    private static void menuEquipos() {
        System.out.println();
        System.out.println("--- EQUIPOS Y MANTENIMIENTO ---");
        System.out.println("1 - Registrar una falla");
        System.out.println("2 - Asignar tecnico a una orden");
        System.out.println("3 - Cerrar una orden");
        System.out.println("4 - Ver los equipos");

        int opcion = leerEntero("Opcion: ");
        switch (opcion) {
            case 1: registrarFalla(); break;
            case 2: asignarTecnico(); break;
            case 3: cerrarOrden(); break;
            case 4: verEquipos(); break;
            default: System.out.println("Opcion invalida.");
        }
    }

    private static void registrarFalla() {
        verEquipos();
        String codigo = leerTexto("Codigo del equipo: ");
        Equipo equipo = gestorMantenimiento.buscarEquipo(codigo);
        if (equipo == null) {
            System.out.println("No existe ese equipo.");
            return;
        }
        Orden_Mantenimiento orden = gestorMantenimiento.registrarFalla(equipo);
        if (orden == null) {
            System.out.println("Ese equipo ya tiene una orden abierta.");
            return;
        }
        System.out.println("Falla reportada por " + guardia.getNombreCompleto()
                + " - " + guardia.getRol());
        System.out.println("Se genero la " + orden);
    }

    private static void asignarTecnico() {
        int numero = leerEntero("Numero de orden: ");
        Orden_Mantenimiento orden = gestorMantenimiento.buscarOrden(numero);
        if (orden == null) {
            System.out.println("No existe esa orden.");
            return;
        }
        for (int i = 0; i < empleados.size(); i++) {
            System.out.println("  " + i + " - " + empleados.get(i).getNombreCompleto());
        }
        int indice = leerEntero("Tecnico: ");
        if (indice < 0 || indice >= empleados.size()) {
            System.out.println("Tecnico invalido.");
            return;
        }
        orden.asignarTecnico(empleados.get(indice));
        System.out.println("Tecnico asignado.");
    }

    private static void cerrarOrden() {
        int numero = leerEntero("Numero de orden: ");
        Orden_Mantenimiento orden = gestorMantenimiento.buscarOrden(numero);
        if (orden == null) {
            System.out.println("No existe esa orden.");
            return;
        }
        String trabajo = leerTexto("Descripcion de la reparacion: ");
        if (orden.cerrarOrden(trabajo)) {
            System.out.println("Orden cerrada.");
        } else {
            System.out.println("No se puede cerrar: falta el tecnico o la descripcion.");
        }
    }

    private static void verEquipos() {
        for (Equipo equipo : gestorMantenimiento.getEquipos()) {
            System.out.println("   " + equipo);
        }
    }

    // ---------- VISITAS ----------

    private static void menuAccesos() {
        System.out.println();
        System.out.println("--- VISITAS ---");
        System.out.println("1 - Autorizar una visita");
        System.out.println("2 - Registrar ingreso");
        System.out.println("3 - Registrar egreso");
        System.out.println("4 - Ver autorizaciones");

        int opcion = leerEntero("Opcion: ");
        try {
            switch (opcion) {
                case 1: autorizarVisita(); break;
                case 2: registrarIngreso(); break;
                case 3: registrarEgreso(); break;
                case 4: verAutorizaciones(); break;
                default: System.out.println("Opcion invalida.");
            }
        } catch (BarrioException e) {
            System.out.println("No se pudo hacer la operacion: " + e.getMessage());
        }
    }

    private static void autorizarVisita() {
        int numeroLote = leerEntero("Numero de lote: ");
        Lote lote = buscarLote(numeroLote);
        if (lote == null) {
            System.out.println("No existe ese lote.");
            return;
        }
        System.out.println("1 - Visita por un dia");
        System.out.println("2 - Proveedor habitual (hasta 30 dias)");
        int tipo = leerEntero("Tipo de autorizacion: ");

        String nombre = leerTexto("Nombre: ");
        String dni = leerTexto("DNI: ");
        String franja = leerTexto("Franja horaria (por ejemplo 08:00-20:00): ");
        String id = "AUT-" + (gestorAccesos.getAutorizaciones().size() + 1);

        Autorizacion autorizacion;
        if (tipo == 2) {
            int dias = leerEntero("Dias de vigencia: ");
            autorizacion = new Autorizacion_Recurrente(id, lote.getTitular(), franja,
                    nombre, dni, dias);
        } else {
            autorizacion = new Autorizacion_Unica(id, lote.getTitular(), franja,
                    nombre, dni, LocalDate.now());
        }
        gestorAccesos.registrarAutorizacion(autorizacion);
        System.out.println("Autorizacion creada: " + autorizacion);
    }

    private static void registrarIngreso() throws BarrioException {
        String id = leerTexto("Id de autorizacion: ");
        String nombre = leerTexto("Nombre: ");
        String dni = leerTexto("DNI: ");
        Visita visita = gestorAccesos.registrarIngreso(id, nombre, dni);
        System.out.println("Ingreso registrado: " + visita);
    }

    private static void registrarEgreso() {
        String dni = leerTexto("DNI: ");
        if (gestorAccesos.registrarEgreso(dni)) {
            System.out.println("Egreso registrado.");
        } else {
            System.out.println("Esa persona no figura adentro del barrio.");
        }
    }

    private static void verAutorizaciones() {
        for (Autorizacion autorizacion : gestorAccesos.getAutorizaciones()) {
            System.out.println("   " + autorizacion + " [" + autorizacion.getEstado() + "]");
        }
    }

    // ---------- REPORTES ----------

    private static void menuReportes() {
        System.out.println();
        System.out.println("--- REPORTES ---");
        System.out.println("1 - Reclamos pendientes");
        System.out.println("2 - Morosidad por lote");
        System.out.println("3 - Equipos fuera de servicio");

        int opcion = leerEntero("Opcion: ");
        if (opcion == 1) {
            List<Reclamo> pendientes =
                    generadorReportes.generarReporteReclamosPendientes(gestorReclamo.getReclamos());
            titulo("Reclamos pendientes (por prioridad)");
            if (pendientes.isEmpty()) {
                System.out.println("   (sin resultados)");
            }
            for (Reclamo reclamo : pendientes) {
                System.out.println("   " + reclamo);
            }
        } else if (opcion == 2) {
            List<Lote> morosos = generadorReportes.generarReporteMorosidad(lotes);
            titulo("Lotes morosos (por deuda)");
            if (morosos.isEmpty()) {
                System.out.println("   (sin resultados)");
            }
            for (Lote lote : morosos) {
                System.out.println("   " + lote + " | deuda " + redondear(lote.getDeudaTotal()));
            }
        } else if (opcion == 3) {
            List<Equipo> fuera = generadorReportes
                    .generarReporteEquiposFueraDeServicio(gestorMantenimiento.getEquipos());
            titulo("Equipos fuera de servicio");
            if (fuera.isEmpty()) {
                System.out.println("   (sin resultados)");
            }
            for (Equipo equipo : fuera) {
                System.out.println("   " + equipo);
            }
        } else {
            System.out.println("Opcion invalida.");
        }
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println(texto + " - " + LocalDate.now());
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    // ---------- DATOS DE PRUEBA ----------

    private static void cargarDatosDePrueba() {
        Titular titular1 = new Titular("30111222", "Ana", "Gomez");
        titular1.setEmail("ana.gomez@correo.com");
        titular1.setTelefono("11-4555-1111");
        Titular titular2 = new Titular("28333444", "Luis", "Perez");
        titular2.setEmail("luis.perez@correo.com");
        titular2.setTelefono("11-4555-2222");
        Titular titular3 = new Titular("33555666", "Mara", "Diaz");
        titular3.setEmail("mara.diaz@correo.com");
        titular3.setTelefono("11-4555-3333");

        Lote lote1 = new Lote(101, 500, titular1);
        Lote lote2 = new Lote(102, 300, titular2);
        Lote lote3 = new Lote(103, 200, titular3);
        lote1.agregarResidente(new Residente("40777888", "Juan", "Gomez", "familiar"));
        lotes.add(lote1);
        lotes.add(lote2);
        lotes.add(lote3);

        empleados.add(new Personal_de_Mantenimiento("27999000", "Carlos", "Ruiz", "L-01", "electricidad"));
        empleados.add(new Personal_de_Mantenimiento("29888777", "Sofia", "Luna", "L-02", "plomeria"));

        guardia = new Personal_de_seguridad("26777666", "Jorge", "Mendez", "L-03", "noche", "porteria");

        gestorMantenimiento.agregarEquipo(new CamaraSeguridad("CAM-01", "acceso", "1080p", 30));
        gestorMantenimiento.agregarEquipo(new Luminaria("LUM-01", "calles internas", "LED", 100));
        gestorMantenimiento.agregarEquipo(new Luminaria("LUM-02", "calles internas", "LED", 100));

        System.out.println("Datos de prueba cargados: 3 lotes, 2 empleados de mantenimiento, "
                + "1 guardia y 3 equipos.");
    }

    private static Lote buscarLote(int numero) {
        for (Lote lote : lotes) {
            if (lote.getNumero() == numero) {
                return lote;
            }
        }
        return null;
    }

    // ---------- LECTURA POR TECLADO ----------

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!teclado.hasNextInt()) {
            teclado.nextLine();
            System.out.print("Tiene que ser un numero. " + mensaje);
        }
        int valor = teclado.nextInt();
        teclado.nextLine();
        return valor;
    }

    private static double leerDecimal(String mensaje) {
        System.out.print(mensaje);
        while (!teclado.hasNextDouble()) {
            teclado.nextLine();
            System.out.print("Tiene que ser un numero. " + mensaje);
        }
        double valor = teclado.nextDouble();
        teclado.nextLine();
        return valor;
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine();
    }
}
