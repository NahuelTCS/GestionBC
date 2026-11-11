package Los_Aromos.gestores;

import Los_Aromos.modelo.CamaraSeguridad;
import Los_Aromos.modelo.Equipo;
import Los_Aromos.modelo.Orden_Mantenimiento;
import java.util.ArrayList;
import java.util.List;

// Se encarga de los equipos y de las ordenes de mantenimiento.
// Esta clase no estaba en el diagrama de la Etapa 1: la agregamos al codificar
// para no dejar estas reglas dentro del Administrador (queda explicado en el informe).
public class GestorMantenimiento {

    private List<Equipo> equipos;
    private List<Orden_Mantenimiento> ordenes;
    private int proximoNumeroDeOrden;

    public GestorMantenimiento() {
        this.equipos = new ArrayList<>();
        this.ordenes = new ArrayList<>();
        this.proximoNumeroDeOrden = 1;
    }

    public void agregarEquipo(Equipo equipo) {
        equipos.add(equipo);
    }

    // Registra la falla de un equipo y genera su orden de mantenimiento.
    // Si ya hay una orden abierta para ese equipo no se genera otra (supuesto S15).
    // La prioridad la decide el propio equipo: cada subclase la calcula a su manera.
    public Orden_Mantenimiento registrarFalla(Equipo equipo) {
        if (buscarOrdenAbierta(equipo) != null) {
            return null;
        }

        equipo.registrarFalla();

        // Cada equipo decide su prioridad; le pasamos cuantos hay fallados en el sector
        int fallasDelSector = contarFallasEnElSector(equipo.getSector());
        Orden_Mantenimiento orden = new Orden_Mantenimiento(proximoNumeroDeOrden,
                equipo, equipo.prioridadDeOrden(fallasDelSector));
        proximoNumeroDeOrden = proximoNumeroDeOrden + 1;
        ordenes.add(orden);
        return orden;
    }

    private int contarFallasEnElSector(String sector) {
        int cantidad = 0;
        for (Equipo equipo : equipos) {
            if (equipo.getSector().equals(sector) && equipo.getEstado().equals("con falla")) {
                cantidad = cantidad + 1;
            }
        }
        return cantidad;
    }

    private Orden_Mantenimiento buscarOrdenAbierta(Equipo equipo) {
        for (Orden_Mantenimiento orden : ordenes) {
            if (orden.getEquipo() == equipo && orden.estaAbierta()) {
                return orden;
            }
        }
        return null;
    }

    // Regla de negocio: si una camara lleva mas de 48 hs sin funcionar se avisa al administrador.
    public List<Equipo> camarasFueraDeServicioHaceMucho(int horas) {
        List<Equipo> avisar = new ArrayList<>();
        if (horas <= 48) {
            return avisar;
        }
        for (Equipo equipo : equipos) {
            if (equipo instanceof CamaraSeguridad && !equipo.getEstado().equals("operativo")) {
                avisar.add(equipo);
            }
        }
        return avisar;
    }

    public Equipo buscarEquipo(String codigo) {
        for (Equipo equipo : equipos) {
            if (equipo.getCodigo().equals(codigo)) {
                return equipo;
            }
        }
        return null;
    }

    public Orden_Mantenimiento buscarOrden(int numero) {
        for (Orden_Mantenimiento orden : ordenes) {
            if (orden.getNumero() == numero) {
                return orden;
            }
        }
        return null;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public List<Orden_Mantenimiento> getOrdenes() {
        return ordenes;
    }
}
