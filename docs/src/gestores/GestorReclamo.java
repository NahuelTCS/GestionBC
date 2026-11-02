package Los_Aromos.gestores;

import Los_Aromos.modelo.Reclamo;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class GestorReclamo {

    // Regla de negocio: Ningún empleado puede tener más de 5 reclamos en curso a la vez
    public void asignarReclamoAEmpleado(Reclamo reclamo, int reclamosActualesEmpleado) {
        if (reclamosActualesEmpleado >= 5) {
            throw new IllegalStateException("El empleado ya cuenta con 5 reclamos en curso (límite máximo permitido).");
        }
        reclamo.cambiarEstado("asignado");
        System.out.println("Reclamo asignado exitosamente a un único responsable.");
    }

    // Regla de negocio: Reclamo de prioridad alta no atendido en 24 horas se escala al administrador
    public void escalarReclamosVencidos(List<Reclamo> listaReclamos) {
        LocalDate ahora = LocalDate.now();
        for (Reclamo r : listaReclamos) {
            if (r.getPrioridad().equals("alta") && r.getEstado().equals("abierto")) {
                long horasTranscurridas = ChronoUnit.HOURS.between(r.getFecha().atStartOfDay(), ahora.atStartOfDay());
                if (horasTranscurridas >= 24) {
                    System.out.println("El reclamo de prioridad alta N° " + r.getNumero() + " supera las 24 horas sin atención.");
                }
            }
        }
    }
}