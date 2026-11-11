package Los_Aromos.gestores;

import Los_Aromos.comparadores.ComparadorLotePorDeuda;
import Los_Aromos.modelo.Equipo;
import Los_Aromos.modelo.Lote;
import Los_Aromos.modelo.Reclamo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Arma los tres reportes que pide el enunciado.
// Se separo del Administrador para que una sola clase se ocupe de los reportes.
public class GeneradorReportes {

    // Reclamos sin cerrar, ordenados por prioridad.
    public List<Reclamo> generarReporteReclamosPendientes(List<Reclamo> reclamos) {
        List<Reclamo> pendientes = new ArrayList<>();
        for (Reclamo reclamo : reclamos) {
            if (!reclamo.getEstado().equals("cerrado")) {
                pendientes.add(reclamo);
            }
        }
        Collections.sort(pendientes);
        return pendientes;
    }

    // Lotes morosos, ordenados de mayor a menor deuda.
    public List<Lote> generarReporteMorosidad(List<Lote> lotes) {
        List<Lote> morosos = new ArrayList<>();
        for (Lote lote : lotes) {
            if (lote.isMoroso()) {
                morosos.add(lote);
            }
        }
        Collections.sort(morosos, new ComparadorLotePorDeuda());
        return morosos;
    }

    // Equipos que no estan operativos.
    public List<Equipo> generarReporteEquiposFueraDeServicio(List<Equipo> equipos) {
        List<Equipo> fueraDeServicio = new ArrayList<>();
        for (Equipo equipo : equipos) {
            if (!equipo.getEstado().equals("operativo")) {
                fueraDeServicio.add(equipo);
            }
        }
        return fueraDeServicio;
    }
}
