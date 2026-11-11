package Los_Aromos.gestores;

import Los_Aromos.excepciones.LimiteReclamosException;
import Los_Aromos.excepciones.TransicionInvalidaException;
import Los_Aromos.modelo.Personal_de_Mantenimiento;
import Los_Aromos.modelo.Reclamo;
import Los_Aromos.repositorios.RepositorioReclamo;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Se encarga de los reclamos. No es una entidad del barrio: es una clase de servicio
// que se creo para sacarle esas responsabilidades al Administrador.
// Trabaja contra la interfaz RepositorioReclamo y no contra los archivos.
public class GestorReclamo {

    private RepositorioReclamo repositorio;
    private List<Reclamo> reclamos;

    public GestorReclamo(RepositorioReclamo repositorio) {
        this.repositorio = repositorio;
        this.reclamos = new ArrayList<>();
    }

    public void registrarReclamo(Reclamo reclamo) {
        reclamos.add(reclamo);
        repositorio.guardar(reclamo);
    }

    // Regla de negocio: ningun empleado puede tener mas de 5 reclamos en curso.
    // Si llego al limite se lanza una excepcion y no se asigna.
    public void asignarReclamoAEmpleado(Reclamo reclamo, Personal_de_Mantenimiento empleado)
            throws LimiteReclamosException, TransicionInvalidaException {

        if (empleado.getReclamosEnCurso() >= 5) {
            throw new LimiteReclamosException("El empleado " + empleado.getNombreCompleto()
                    + " ya tiene 5 reclamos en curso.");
        }

        reclamo.cambiarEstado("asignado", "Asignado a " + empleado.getNombreCompleto());
        reclamo.asignarResponsable(empleado);
        empleado.sumarReclamo();
        repositorio.guardar(reclamo);
    }

    // Regla de negocio: un reclamo de prioridad alta que no fue atendido
    // en 24 horas se escala al administrador.
    public List<Reclamo> escalarReclamosVencidos() {
        List<Reclamo> escalados = new ArrayList<>();
        LocalDate hoy = LocalDate.now();

        for (Reclamo reclamo : reclamos) {
            long horas = ChronoUnit.HOURS.between(reclamo.getFecha().atStartOfDay(),
                    hoy.atStartOfDay());
            if (reclamo.getPrioridad().equals("alta") && !reclamo.fueAtendido() && horas >= 24) {
                reclamo.escalar();
                escalados.add(reclamo);
                repositorio.guardar(reclamo);
            }
        }
        return escalados;
    }

    // Devuelve los reclamos sin cerrar, ordenados por prioridad (orden natural).
    public List<Reclamo> listarPendientes() {
        List<Reclamo> pendientes = new ArrayList<>();
        for (Reclamo reclamo : reclamos) {
            if (!reclamo.getEstado().equals("cerrado")) {
                pendientes.add(reclamo);
            }
        }
        Collections.sort(pendientes);
        return pendientes;
    }

    public List<Reclamo> getReclamos() {
        return reclamos;
    }

    public Reclamo buscarPorNumero(int numero) {
        for (Reclamo reclamo : reclamos) {
            if (reclamo.getNumero() == numero) {
                return reclamo;
            }
        }
        return null;
    }
}
