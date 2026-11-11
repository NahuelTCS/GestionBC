package Los_Aromos.modelo;

import Los_Aromos.excepciones.TransicionInvalidaException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// Reclamo presentado por un vecino.
// Implementa Comparable para que el orden natural sea por prioridad:
// primero los de prioridad alta.
public class Reclamo implements Comparable<Reclamo> {

    private int numero;
    private LocalDate fecha;
    private String tipo;        // seguridad, mantenimiento, convivencia, administrativo
    private String descripcion;
    private String prioridad;   // alta, media, baja
    private String estado;      // abierto, asignado, en curso, resuelto, cerrado
    private LocalDate fechaResolucion;
    private Lote lote;
    private Persona autor;
    private Personal_de_Mantenimiento responsable;
    private List<Comentario> comentarios;
    private List<CambioEstado> cambios;

    public Reclamo(int numero, String tipo, String descripcion, Lote lote, Persona autor) {
        this.numero = numero;
        this.fecha = LocalDate.now();
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.lote = lote;
        this.autor = autor;
        this.estado = "abierto";
        this.comentarios = new ArrayList<>();
        this.cambios = new ArrayList<>();
        this.prioridad = calcularPrioridad(tipo);
    }

    // Regla de negocio: la prioridad sale del tipo de reclamo.
    private String calcularPrioridad(String tipo) {
        if (tipo.equalsIgnoreCase("seguridad")) {
            return "alta";
        }
        if (tipo.equalsIgnoreCase("mantenimiento")) {
            return "media";
        }
        return "baja";
    }

    // Regla de negocio: solo se puede avanzar al estado siguiente.
    // El orden es abierto, asignado, en curso, resuelto, cerrado.
    public void cambiarEstado(String nuevoEstado, String observacion)
            throws TransicionInvalidaException {

        if (!esEstadoSiguiente(this.estado, nuevoEstado)) {
            throw new TransicionInvalidaException("No se puede pasar de '" + this.estado
                    + "' a '" + nuevoEstado + "'. El orden es: abierto, asignado, en curso, resuelto, cerrado.");
        }
        this.estado = nuevoEstado;
        if (nuevoEstado.equals("resuelto")) {
            this.fechaResolucion = LocalDate.now();
        }
        cambios.add(new CambioEstado(nuevoEstado, observacion));
    }

    private boolean esEstadoSiguiente(String actual, String siguiente) {
        if (actual.equals("abierto") && siguiente.equals("asignado")) {
            return true;
        }
        if (actual.equals("asignado") && siguiente.equals("en curso")) {
            return true;
        }
        if (actual.equals("en curso") && siguiente.equals("resuelto")) {
            return true;
        }
        if (actual.equals("resuelto") && siguiente.equals("cerrado")) {
            return true;
        }
        return false;
    }

    // Regla de negocio: el vecino puede reabrir un reclamo resuelto dentro de los 5 dias.
    // Al reabrirse vuelve al estado "en curso" (supuesto S9 del analisis).
    // Pasado el plazo, el reclamo se cierra.
    public boolean reabrir(LocalDate fechaActual) {
        if (!estado.equals("resuelto")) {
            return false;
        }
        long dias = ChronoUnit.DAYS.between(fechaResolucion, fechaActual);
        if (dias <= 5) {
            this.estado = "en curso";
            cambios.add(new CambioEstado("en curso", "Reabierto por el vecino"));
            return true;
        }
        this.estado = "cerrado";
        cambios.add(new CambioEstado("cerrado", "Cerrado por vencimiento del plazo de 5 dias"));
        return false;
    }

    // Regla de negocio: un reclamo de prioridad alta sin atender en 24 hs se escala.
    public void escalar() {
        cambios.add(new CambioEstado(estado, "Escalado al administrador por superar las 24 hs"));
    }

    // Un reclamo se considera atendido cuando llego al menos a "en curso" (supuesto S8).
    public boolean fueAtendido() {
        return estado.equals("en curso") || estado.equals("resuelto") || estado.equals("cerrado");
    }

    public void agregarComentario(String texto, String autorComentario) {
        comentarios.add(new Comentario(texto, autorComentario));
    }

    // Orden natural: primero la prioridad mas alta y, si empatan, el mas viejo.
    @Override
    public int compareTo(Reclamo otro) {
        int comparacion = pesoPrioridad(this.prioridad) - pesoPrioridad(otro.prioridad);
        if (comparacion != 0) {
            return comparacion;
        }
        return this.fecha.compareTo(otro.fecha);
    }

    private int pesoPrioridad(String prioridad) {
        if (prioridad.equals("alta")) {
            return 1;
        }
        if (prioridad.equals("media")) {
            return 2;
        }
        return 3;
    }

    public void asignarResponsable(Personal_de_Mantenimiento responsable) {
        this.responsable = responsable;
    }

    public int getNumero() {
        return numero;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Lote getLote() {
        return lote;
    }

    public Persona getAutor() {
        return autor;
    }

    public Personal_de_Mantenimiento getResponsable() {
        return responsable;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public List<CambioEstado> getCambios() {
        return cambios;
    }

    @Override
    public String toString() {
        return "Reclamo " + numero + " | " + tipo + " | prioridad " + prioridad
                + " | estado " + estado + " | lote " + lote.getNumero();
    }
}
