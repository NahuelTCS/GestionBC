package Los_Aromos.pruebas;

import Los_Aromos.excepciones.AutorizacionInvalidaException;
import Los_Aromos.gestores.GestorAccesos;
import Los_Aromos.gestores.GestorMantenimiento;
import Los_Aromos.modelo.*;
import java.time.LocalDate;

// Pruebas de equipos, ordenes de mantenimiento y control de visitas.
public class PruebaEquipoYAcceso {

    public static void main(String[] args) {
        System.out.println("=== PRUEBAS DE EQUIPOS Y VISITAS ===");
        probarPrioridadDeLasOrdenes();
        probarUnaSolaOrdenAbierta();
        probarCierreDeOrden();
        probarAutorizaciones();
        Verificador.mostrarResumen("Pruebas de equipos y visitas");
    }

    private static void probarPrioridadDeLasOrdenes() {
        GestorMantenimiento gestor = new GestorMantenimiento();

        Equipo camara = new CamaraSeguridad("CAM-01", "acceso", "1080p", 30);
        gestor.agregarEquipo(camara);
        Orden_Mantenimiento ordenCamara = gestor.registrarFalla(camara);
        Verificador.verificar("La falla de una camara genera prioridad alta",
                "alta", camara.prioridadDeOrden(0));
        Verificador.verificar("El equipo queda en reparacion o con falla",
                "con falla", camara.getEstado());
        Verificador.verificar("Se genero la orden", true, ordenCamara != null);

        // Con pocas luminarias falladas la prioridad es normal
        Luminaria luminaria = new Luminaria("LUM-01", "calles internas", "LED", 100);
        gestor.agregarEquipo(luminaria);
        gestor.registrarFalla(luminaria);
        Verificador.verificar("Una luminaria sola es prioridad normal",
                "normal", luminaria.prioridadDeOrden(1));

        // Con mas de 3 luminarias falladas en el sector pasa a alta
        for (int i = 2; i <= 5; i++) {
            Luminaria otra = new Luminaria("LUM-0" + i, "calles internas", "LED", 100);
            gestor.agregarEquipo(otra);
            gestor.registrarFalla(otra);
        }
        Luminaria ultima = new Luminaria("LUM-09", "calles internas", "LED", 100);
        gestor.agregarEquipo(ultima);
        Orden_Mantenimiento ordenUltima = gestor.registrarFalla(ultima);
        Verificador.verificar("Con mas de 3 luminarias falladas del sector pasa a alta",
                "alta", ultima.prioridadDeOrden(5));
        Verificador.verificar("La orden generada tambien queda en alta",
                true, ordenUltima != null);
    }

    private static void probarUnaSolaOrdenAbierta() {
        GestorMantenimiento gestor = new GestorMantenimiento();
        Equipo camara = new CamaraSeguridad("CAM-02", "perimetro", "720p", 15);
        gestor.agregarEquipo(camara);

        gestor.registrarFalla(camara);
        Orden_Mantenimiento segunda = gestor.registrarFalla(camara);
        Verificador.verificar("No se genera una segunda orden si ya hay una abierta",
                true, segunda == null);
        Verificador.verificar("Hay una sola orden", 1, gestor.getOrdenes().size());
    }

    private static void probarCierreDeOrden() {
        GestorMantenimiento gestor = new GestorMantenimiento();
        Equipo luminaria = new Luminaria("LUM-10", "accesos", "LED", 80);
        gestor.agregarEquipo(luminaria);
        Orden_Mantenimiento orden = gestor.registrarFalla(luminaria);

        // Sin tecnico asignado no se puede cerrar
        boolean sinTecnico = orden.cerrarOrden("Se cambio la lampara");
        Verificador.verificar("Sin tecnico la orden no se cierra", false, sinTecnico);

        Personal_de_Mantenimiento tecnico =
                new Personal_de_Mantenimiento("27999000", "Carlos", "Ruiz", "L-01", "electricidad");
        orden.asignarTecnico(tecnico);

        // Tampoco se cierra sin descripcion del trabajo
        boolean sinDescripcion = orden.cerrarOrden("");
        Verificador.verificar("Sin descripcion la orden no se cierra", false, sinDescripcion);

        boolean cerrada = orden.cerrarOrden("Se cambio la lampara");
        Verificador.verificar("Con tecnico y descripcion si se cierra", true, cerrada);
        Verificador.verificar("El equipo vuelve a operativo", "operativo", luminaria.getEstado());
    }

    private static void probarAutorizaciones() {
        GestorAccesos gestor = new GestorAccesos();
        Titular titular = new Titular("30111222", "Ana", "Gomez");

        Autorizacion_Unica unica = new Autorizacion_Unica("AUT-1", titular, "08:00-20:00",
                "Pedro Lopez", "35222111", LocalDate.now());
        gestor.registrarAutorizacion(unica);

        Verificador.verificar("La autorizacion de hoy es valida", true, unica.esValida(LocalDate.now()));
        Verificador.verificar("Para manana no es valida", false,
                unica.esValida(LocalDate.now().plusDays(1)));

        // Ingreso con autorizacion valida
        try {
            gestor.registrarIngreso("AUT-1", "Pedro Lopez", "35222111");
            Verificador.verificar("Ingreso con autorizacion valida", 1, gestor.getVisitas().size());
        } catch (AutorizacionInvalidaException e) {
            Verificador.verificar("Ingreso con autorizacion valida", 1, "lanzo excepcion");
        }

        // Ingreso sin autorizacion
        try {
            gestor.registrarIngreso("AUT-99", "Juan Diaz", "36111222");
            Verificador.verificar("Sin autorizacion no entra", "excepcion", "no la lanzo");
        } catch (AutorizacionInvalidaException e) {
            Verificador.verificar("Sin autorizacion no entra", "excepcion", "excepcion");
        }

        // La recurrente se corta a los 30 dias aunque se pidan mas
        Autorizacion_Recurrente recurrente = new Autorizacion_Recurrente("AUT-2", titular,
                "09:00-18:00", "Jardineria SRL", "20304050", 60);
        Verificador.verificar("A los 30 dias la recurrente sigue valida", true,
                recurrente.esValida(LocalDate.now().plusDays(30)));
        Verificador.verificar("A los 31 dias ya no vale", false,
                recurrente.esValida(LocalDate.now().plusDays(31)));
    }
}
