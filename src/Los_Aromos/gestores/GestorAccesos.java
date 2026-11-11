package Los_Aromos.gestores;

import Los_Aromos.excepciones.AutorizacionInvalidaException;
import Los_Aromos.modelo.Autorizacion;
import Los_Aromos.modelo.Visita;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Se encarga de las autorizaciones y del ingreso y egreso de visitas.
// Tampoco estaba en el diagrama de la Etapa 1 (queda explicado en el informe).
public class GestorAccesos {

    private List<Autorizacion> autorizaciones;
    private List<Visita> visitas;

    public GestorAccesos() {
        this.autorizaciones = new ArrayList<>();
        this.visitas = new ArrayList<>();
    }

    public void registrarAutorizacion(Autorizacion autorizacion) {
        autorizaciones.add(autorizacion);
    }

    // Regla de negocio: sin autorizacion valida, seguridad no habilita el ingreso.
    // Ademas, una persona no puede volver a entrar sin haber salido antes.
    public Visita registrarIngreso(String idAutorizacion, String nombre, String dni)
            throws AutorizacionInvalidaException {

        Autorizacion autorizacion = buscarAutorizacion(idAutorizacion);
        if (autorizacion == null) {
            throw new AutorizacionInvalidaException("No existe la autorizacion " + idAutorizacion);
        }
        if (!autorizacion.esValida(LocalDate.now())) {
            throw new AutorizacionInvalidaException("La autorizacion " + idAutorizacion
                    + " no esta vigente para hoy.");
        }

        Visita anterior = buscarVisitaAdentro(dni);
        if (anterior != null) {
            throw new AutorizacionInvalidaException("La persona con DNI " + dni
                    + " ya esta adentro del barrio.");
        }

        Visita visita = new Visita(nombre, dni);
        visita.registrarIngreso();
        visitas.add(visita);
        autorizacion.marcarUsada();
        return visita;
    }

    public boolean registrarEgreso(String dni) {
        Visita visita = buscarVisitaAdentro(dni);
        if (visita == null) {
            return false;
        }
        visita.registrarEgreso();
        return true;
    }

    private Visita buscarVisitaAdentro(String dni) {
        for (Visita visita : visitas) {
            if (visita.getDni().equals(dni) && visita.estaAdentro()) {
                return visita;
            }
        }
        return null;
    }

    public Autorizacion buscarAutorizacion(String id) {
        for (Autorizacion autorizacion : autorizaciones) {
            if (autorizacion.getIdAutorizacion().equals(id)) {
                return autorizacion;
            }
        }
        return null;
    }

    public List<Autorizacion> getAutorizaciones() {
        return autorizaciones;
    }

    public List<Visita> getVisitas() {
        return visitas;
    }
}
