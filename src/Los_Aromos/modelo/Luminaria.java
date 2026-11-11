package Los_Aromos.modelo;

// Luminaria del barrio. La orden es de prioridad normal, pero sube a alta
// cuando ya hay mas de 3 luminarias con falla en el mismo sector.
public class Luminaria extends Equipo {

    private String tipoLampara;
    private int potencia;

    public Luminaria(String codigo, String sector, String tipoLampara, int potencia) {
        super(codigo, sector);
        this.tipoLampara = tipoLampara;
        this.potencia = potencia;
    }

    @Override
    public String prioridadDeOrden(int fallasDelSector) {
        if (fallasDelSector > 3) {
            return "alta";
        }
        return "normal";
    }

    @Override
    public String toString() {
        return "Luminaria " + super.toString() + " | " + tipoLampara
                + " " + potencia + "W";
    }
}
