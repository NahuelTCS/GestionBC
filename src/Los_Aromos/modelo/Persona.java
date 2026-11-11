package Los_Aromos.modelo;

// Clase base de todas las personas del sistema. Cada subclase dice que rol cumple.
public abstract class Persona {

    protected String dni;
    protected String nombre;
    protected String apellido;
    protected String telefono;
    protected String email;

    public Persona(String dni, String nombre, String apellido) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    // Cada subclase indica que rol cumple dentro del barrio.
    public abstract String getRol();

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public String getDni() {
        return dni;
    }

    // Datos de contacto, para las notificaciones al titular
    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (" + getRol() + ")";
    }
}
