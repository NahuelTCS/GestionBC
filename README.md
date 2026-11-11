# Sistema de gestión - Barrio cerrado "Los Aromos"

Trabajo Práctico Obligatorio de Programación Orientada a Objetos - UADE, 2° cuatrimestre 2026.
Propuesta A: gestión de un barrio cerrado de 150 lotes.

## Integrantes

- Rodrigo Peña Fernandez
- Nahuel Tomás Caballero

## Qué hace el sistema

Ordena la administración del barrio, que hoy se maneja con planillas sueltas:

- **Reclamos.** Alta, asignación a un empleado, seguimiento del estado, reapertura y cierre.
- **Expensas y pagos.** Emisión mensual por lote, cálculo de intereses por mora,
  registro de pagos con distintos medios y control de la morosidad.
- **Equipos.** Registro de fallas de cámaras y luminarias, generación y cierre de
  órdenes de mantenimiento, y consulta del estado de cada equipo.
- **Visitas.** Autorizaciones únicas y recurrentes, y registro de ingreso y egreso.
- **Reportes.** Reclamos pendientes, morosidad por lote y equipos fuera de servicio.

## Requisitos

- Java 17 o superior (se desarrolló con Java 21).
- No usa librerías externas.

## Cómo ejecutarlo

Desde la raíz del proyecto:

```
javac -d out -encoding UTF-8 -sourcepath src src/Los_Aromos/Main.java
java -cp out Los_Aromos.Main
```

En Windows, si los acentos se ven mal en la consola, ejecutar antes `chcp 65001`.

También se puede abrir la carpeta en IntelliJ IDEA, marcar `src` como *Sources Root*
y `test` como *Test Sources Root*, y ejecutar `Main`.

## Cómo ejecutar las pruebas

```
javac -d out -encoding UTF-8 -sourcepath "src;test" src/Los_Aromos/Main.java test/Los_Aromos/pruebas/*.java
java -cp out Los_Aromos.pruebas.PruebaReclamo
java -cp out Los_Aromos.pruebas.PruebaExpensaYPago
java -cp out Los_Aromos.pruebas.PruebaEquipoYAcceso
```

En Linux o Mac, el separador del `sourcepath` es `:` en lugar de `;`.

Cada prueba imprime una línea por caso con el resultado esperado y el obtenido,
y un resumen al final. El detalle está en `docs/plan-de-pruebas.md`.

## Datos de prueba

Al arrancar, el programa carga solos estos datos para poder probar sin cargar nada a mano:

| Dato | Valores |
|---|---|
| Lotes | 101 (500 m2, Ana Gomez), 102 (300 m2, Luis Perez), 103 (200 m2, Mara Diaz) |
| Residentes | Juan Gomez (familiar) en el lote 101 |
| Empleados | Carlos Ruiz (electricidad), Sofia Luna (plomería) |
| Seguridad | Jorge Mendez (turno noche, portería) |
| Equipos | CAM-01 (cámara, acceso), LUM-01 y LUM-02 (luminarias, calles internas) |

### Un recorrido rápido para probar

1. `2` → `1`, período `2026-11`, presupuesto `900000`. Emite las expensas de los tres lotes.
2. `2` → `2`, lote `101`, importe `450000`, medio `1`. Registra el pago en efectivo.
3. `1` → `1`, lote `101`, tipo `seguridad`, descripción libre. Crea un reclamo de prioridad alta.
4. `1` → `2`, reclamo `1`, empleado `0`. Lo asigna a Carlos Ruiz.
5. `3` → `1`, equipo `CAM-01`. Registra la falla y genera la orden de prioridad alta.
6. `2` → `4`, lote `101`, expensa `0`, tipo `en contra`, motivo libre, importe `5000`. Registra un ajuste sin borrar el pago.
7. `4` → `1`, lote `101`, tipo `2`, 15 días. Crea una autorización para un proveedor habitual.
8. `5` → `1`. Muestra los reclamos pendientes ordenados por prioridad.

## Archivos que genera

El programa guarda los datos en archivos de texto en la carpeta desde donde se ejecuta,
así la información se conserva entre ejecuciones:

- `lotes.txt`
- `expensas.txt`
- `reclamos.txt`

Para arrancar de cero, basta con borrarlos.

## Estructura del proyecto

```
/docs                      informe, plan de pruebas y documento de la Etapa 1
/docs/uml                  diagramas (imagen y archivo fuente)
/src/Los_Aromos            código fuente
    /modelo                entidades del barrio
    /gestores              clases de servicio
    /repositorios          interfaces de persistencia y su implementación en archivos
    /excepciones           excepciones propias de las reglas de negocio
    /comparadores          ordenamientos personalizados
/test/Los_Aromos/pruebas   pruebas unitarias
```
