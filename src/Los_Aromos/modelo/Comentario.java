package Los_Aromos.modelo;

import java.time.LocalDateTime;

public class Comentario {

    private LocalDateTime fechaHora;
    private String texto;
    private String autor;

    public Comentario(String texto, String autor) {
        this.fechaHora = LocalDateTime.now();
        this.texto = texto;
        this.autor = autor;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    @Override
    public String toString() {
        return fechaHora.toLocalDate() + " - " + autor + ": " + texto;
    }
}
