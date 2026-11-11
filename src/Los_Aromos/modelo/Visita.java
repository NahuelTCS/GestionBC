package Los_Aromos.modelo;

import java.time.LocalTime;

// Persona que entra al barrio con una autorizacion del vecino.
public class Visita {

    private String nombre;
    private String dni;
    private LocalTime horaIngreso;
    private LocalTime horaEgreso;

    public Visita(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
    }

    public void registrarIngreso() {
        this.horaIngreso = LocalTime.now();
    }

    public void registrarEgreso() {
        this.horaEgreso = LocalTime.now();
    }

    // No se puede registrar un segundo ingreso sin haber salido antes.
    public boolean estaAdentro() {
        return horaIngreso != null && horaEgreso == null;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre + " (DNI " + dni + ")";
    }
}
