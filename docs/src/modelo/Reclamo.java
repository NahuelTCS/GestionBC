package Los_Aromos.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Reclamo {
    private int numero;
    private LocalDate fecha;
    private String tipo; // seguridad, mantenimiento, convivencia, administrativo
    private String descripcion;
    private String prioridad; // seguridad = alta, mantenimiento = media, otros = baja
    private String estado; // abierto, asignado, en curso, resuelto, cerrado
    private List<String> comentarios;

    public Reclamo(int numero, String tipo, String descripcion) {
        this.numero = numero;
        this.fecha = LocalDate.now();
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.estado = "abierto";
        this.comentarios = new ArrayList<>();

        // Regla de negocio: Prioridades según tipo
        if (tipo.equalsIgnoreCase("seguridad")) {
            this.prioridad = "alta";
        } else if (tipo.equalsIgnoreCase("mantenimiento")) {
            this.prioridad = "media";
        } else {
            this.prioridad = "baja";
        }
    }

    // Regla de negocio: Solo se puede avanzar al estado siguiente de forma estricta
    public void cambiarEstado(String nuevoEstado) {
        if (esEstadoSiguiente(this.estado, nuevoEstado)) {
            this.estado = nuevoEstado;
            System.out.println("El reclamo " + numero + " avanzó al estado: " + estado);
        } else {
            throw new IllegalArgumentException("Transición de estado inválida. Debe seguir la secuencia: abierto -> asignado -> en curso -> resuelto -> cerrado.");
        }
    }

    private boolean esEstadoSiguiente(String actual, String siguiente) {
        if (actual.equals("abierto") && siguiente.equals("asignado")) return true;
        if (actual.equals("asignado") && siguiente.equals("en curso")) return true;
        if (actual.equals("en curso") && siguiente.equals("resuelto")) return true;
        if (actual.equals("resuelto") && siguiente.equals("cerrado")) return true;
        return false;
    }

    // Regla de negocio: El vecino puede reabrir un reclamo resuelto dentro de los 5 días
    public void reabrir(LocalDate fechaActual) {
        if (this.estado.equals("resuelto")) {
            long diasTranscurridos = ChronoUnit.DAYS.between(this.fecha, fechaActual);
            if (diasTranscurridos <= 5) {
                this.estado = "abierto";
                System.out.println("El reclamo " + numero + " ha sido reabierto por el vecino.");
            } else {
                this.estado = "cerrado";
                System.out.println("Plazo de 5 días vencido. El reclamo " + numero + " pasa a estado cerrado.");
            }
        }
    }

    public void agregarComentario(String comentario) {
        this.comentarios.add(comentario);
    }

    public int getNumero() { return numero; }
    public String getTipo() { return tipo; }
    public String getPrioridad() { return prioridad; }
    public String getEstado() { return estado; }
    public LocalDate getFecha() { return fecha; }
}