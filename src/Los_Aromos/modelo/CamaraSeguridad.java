package Los_Aromos.modelo;

public class CamaraSeguridad extends Equipo {

    private String resolucion;
    private int diasGrabacion;

    public CamaraSeguridad(String codigo, String sector, String resolucion, int diasGrabacion) {
        super(codigo, sector);
        this.resolucion = resolucion;
        this.diasGrabacion = diasGrabacion;
    }

    @Override
    public String prioridadDeOrden(int fallasDelSector) {
        // Una camara rota es siempre urgente, no importa el resto del sector
        return "alta";
    }

    @Override
    public String toString() {
        return "Camara " + super.toString() + " | " + resolucion
                + " | guarda " + diasGrabacion + " dias";
    }
}
