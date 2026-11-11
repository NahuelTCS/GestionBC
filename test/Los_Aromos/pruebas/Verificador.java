package Los_Aromos.pruebas;

// Metodos que usan las pruebas para comparar lo esperado con lo obtenido.
public class Verificador {

    private static int pasaron = 0;
    private static int fallaron = 0;

    public static void verificar(String caso, Object esperado, Object obtenido) {
        boolean ok = String.valueOf(esperado).equals(String.valueOf(obtenido));
        if (ok) {
            pasaron = pasaron + 1;
            System.out.println("OK    | " + caso + " | esperado y obtenido: " + obtenido);
        } else {
            fallaron = fallaron + 1;
            System.out.println("FALLA | " + caso + " | esperado: " + esperado
                    + " | obtenido: " + obtenido);
        }
    }

    public static void mostrarResumen(String nombrePrueba) {
        System.out.println("---------------------------------------------");
        System.out.println(nombrePrueba + ": " + pasaron + " correctas, " + fallaron + " con error.");
        pasaron = 0;
        fallaron = 0;
    }
}
