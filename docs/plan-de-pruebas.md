# Plan de pruebas - Etapa 2

## 1. Pruebas unitarias

Están en `/test/Los_Aromos/pruebas`. No usamos ninguna librería de testing porque no la
vimos en la cursada: cada prueba es una clase con `main` que ejecuta un caso y compara el
resultado esperado con el obtenido usando la clase `Verificador`, que imprime `OK` o `FALLA`
y un resumen al final.

Cómo ejecutarlas: está explicado en el README.

### PruebaReclamo (18 casos)

| # | Caso | Entrada | Resultado esperado | Resultado obtenido |
|---|---|---|---|---|
| 1 | Prioridad de un reclamo de seguridad | tipo "seguridad" | alta | alta |
| 2 | Prioridad de uno de mantenimiento | tipo "mantenimiento" | media | media |
| 3 | Prioridad de uno de convivencia | tipo "convivencia" | baja | baja |
| 4 | Estado inicial | reclamo nuevo | abierto | abierto |
| 5 | Avance de estado válido | abierto a asignado | asignado | asignado |
| 6 | Saltear estados | asignado a cerrado | TransicionInvalidaException | excepción lanzada |
| 7 | El estado no cambia tras el error | asignado a cerrado | asignado | asignado |
| 8 | Se registra el historial | 1 cambio hecho | 1 registro | 1 registro |
| 9 | Carga del empleado | 5 asignaciones | 5 reclamos en curso | 5 |
| 10 | Límite de 5 reclamos | asignar el sexto | LimiteReclamosException | excepción lanzada |
| 11 | El reclamo rechazado no cambia | sexto reclamo | abierto | abierto |
| 12 | Reapertura dentro del plazo | resuelto + 3 días | true | true |
| 13 | Estado al reabrir | resuelto + 3 días | en curso | en curso |
| 14 | Reapertura fuera de plazo | resuelto + 10 días | false | false |
| 15 | Estado pasado el plazo | resuelto + 10 días | cerrado | cerrado |
| 16 | Orden natural, primero | 3 reclamos mezclados | alta | alta |
| 17 | Orden natural, segundo | 3 reclamos mezclados | media | media |
| 18 | Orden natural, último | 3 reclamos mezclados | baja | baja |

### PruebaExpensaYPago (17 casos)

| # | Caso | Entrada | Resultado esperado | Resultado obtenido |
|---|---|---|---|---|
| 1 | Cálculo de la expensa | presupuesto 1.000.000, lote 500 de 1000 m2 | 500.000 | 500.000 |
| 2 | Sin intereses antes del vencimiento | 5 días antes | 0 | 0 |
| 3 | Interés a un mes | 100.000 vencidos hace 1 mes | 3.000 | 3.000 |
| 4 | El pago cubre primero los intereses | pago de 3.000 | intereses en 0 | 0 |
| 5 | El capital no se toca | pago de 3.000 | 100.000 | 100.000 |
| 6 | Estado tras pagar solo intereses | pago de 3.000 | pendiente | pendiente |
| 7 | Pago parcial del capital | pago de 50.000 | saldo 50.000 | 50.000 |
| 8 | Estado con pago parcial | pago de 50.000 | parcial | parcial |
| 9 | Pago total | pago de 60.000 | pagada | pagada |
| 10 | Sobrante del pago | pago de 60.000 sobre saldo 50.000 | 10.000 | 10.000 |
| 11 | Pago rechazado por la tarjeta | 600.000 con límite 500.000 | PagoRechazadoException | excepción lanzada |
| 12 | La expensa no se modifica si se rechaza | 600.000 | saldo 600.000 | 600.000 |
| 13 | Pago aceptado en efectivo | capital + intereses | pagada | pagada |
| 14 | Pago del capital sin los intereses | solo el capital | parcial | parcial |
| 15 | 3 períodos impagos | 3 expensas vencidas | no moroso | false |
| 16 | 4 períodos impagos | 4 expensas vencidas | moroso | true |
| 17 | Regularización de la deuda | se paga todo | no moroso | false |

### PruebaEquipoYAcceso (18 casos)

| # | Caso | Entrada | Resultado esperado | Resultado obtenido |
|---|---|---|---|---|
| 1 | Prioridad por falla de cámara | falla en CAM-01 | alta | alta |
| 2 | Estado del equipo tras la falla | falla en CAM-01 | con falla | con falla |
| 3 | Se genera la orden | falla en CAM-01 | orden creada | creada |
| 4 | Prioridad de una luminaria sola | falla en LUM-01 | normal | normal |
| 5 | Más de 3 luminarias del sector | 6 luminarias con falla | alta | alta |
| 5b | La orden generada queda en alta | 6 luminarias con falla | orden creada | creada |
| 6 | Segunda orden sobre el mismo equipo | falla repetida | no se genera | null |
| 7 | Cantidad de órdenes | falla repetida | 1 | 1 |
| 8 | Cierre sin técnico | cerrar sin asignar | false | false |
| 9 | Cierre sin descripción | descripción vacía | false | false |
| 10 | Cierre correcto | técnico + descripción | true | true |
| 11 | Estado del equipo al cerrar | orden cerrada | operativo | operativo |
| 12 | Autorización única válida hoy | fecha de hoy | true | true |
| 13 | Autorización única mañana | fecha de mañana | false | false |
| 14 | Ingreso con autorización válida | AUT-1 | visita registrada | 1 visita |
| 15 | Ingreso sin autorización | AUT-99 | AutorizacionInvalidaException | excepción lanzada |
| 16 | Recurrente a los 30 días | vigencia pedida de 60 | true | true |
| 17 | Recurrente a los 31 días | vigencia pedida de 60 | false | false |

**Resultado: 53 casos, 53 correctos.**

## 2. Plan de pruebas funcionales

Se ejecutaron desde el menú por consola, con los datos de prueba que carga el programa.

| # | Caso de uso | Pasos y datos de entrada | Resultado esperado | Resultado obtenido |
|---|---|---|---|---|
| F1 | Emitir expensas (CU7) | Menú 2 → 1, período 2026-11, presupuesto 900000 | Tres expensas: 450.000, 270.000 y 180.000 según superficie | Correcto |
| F2 | Registrar pago (CU8) | Menú 2 → 2, lote 101, importe 450000, medio 1 | Pago aceptado y expensa saldada | Correcto |
| F3 | Pago rechazado (CU8, flujo de error) | Menú 2 → 2, lote 101, importe 600000, medio 2 | Mensaje de rechazo y la expensa queda igual | Correcto |
| F4 | Presentar reclamo (CU1) | Menú 1 → 1, lote 101, tipo seguridad | Reclamo creado con prioridad alta y estado abierto | Correcto |
| F5 | Asignar reclamo (CU2) | Menú 1 → 2, reclamo 1, empleado 0 | Reclamo en estado asignado | Correcto |
| F6 | Límite de 5 reclamos (CU2, flujo de error) | Asignar 6 reclamos al mismo empleado | Mensaje de error en el sexto | Correcto |
| F7 | Cambiar estado (CU3) | Menú 1 → 3, reclamo 1, estado "en curso" | El reclamo avanza y queda el registro | Correcto |
| F8 | Transición inválida (CU3, flujo de error) | Menú 1 → 3, reclamo 1, estado "cerrado" | Mensaje de error y el estado no cambia | Correcto |
| F9 | Reabrir reclamo (CU4) | Menú 1 → 4, reclamo resuelto | Vuelve a "en curso" | Correcto |
| F10 | Ver reclamo (CU5) | Menú 1 → 5, reclamo 1 | Muestra estado, historial y comentarios | Correcto |
| F11 | Registrar falla (CU10) | Menú 3 → 1, equipo CAM-01 | Orden de prioridad alta | Correcto |
| F12 | Segunda falla del mismo equipo (CU10) | Repetir sobre CAM-01 | Avisa que ya hay una orden abierta | Correcto |
| F13 | Cerrar orden (CU11) | Menú 3 → 3 sin técnico asignado | No deja cerrar | Correcto |
| F13b | Registrar ajuste (CU9) | Menú 2 → 4, lote 101, tipo "en contra", importe 5000 | El saldo sube 5000 y el pago anterior no se borra | Correcto |
| F13c | Ajuste sin motivo (CU9, flujo de error) | Menú 2 → 4, motivo vacío | No se registra el ajuste | Correcto |
| F14 | Autorizar visita por un día (CU13) | Menú 4 → 1, tipo 1, lote 101 | Autorización única creada para hoy | Correcto |
| F14b | Autorizar proveedor habitual (CU13) | Menú 4 → 1, tipo 2, 15 días | Autorización recurrente del día de hoy a 15 días | Correcto |
| F14c | Vigencia mayor a 30 días (CU13) | Menú 4 → 1, tipo 2, 60 días | Se recorta a 30 días | Correcto |
| F15 | Ingreso de visita (CU14) | Menú 4 → 2, id AUT-1 | Ingreso registrado con la hora | Correcto |
| F16 | Ingreso sin autorización (CU14, error) | Menú 4 → 2, id inexistente | Mensaje de autorización inválida | Correcto |
| F16b | Listar reclamos por antigüedad (CU5) | Menú 1 → 7 | Lista ordenada del más viejo al más nuevo | Correcto |
| F17 | Reporte de pendientes (CU15) | Menú 5 → 1 | Lista ordenada por prioridad | Correcto |
| F18 | Reporte de morosidad (CU15) | Menú 5 → 2 | Lotes morosos ordenados por deuda | Correcto |
| F19 | Reporte de equipos (CU15) | Menú 5 → 3 | Equipos que no están operativos | Correcto |
| F20 | Persistencia entre ejecuciones | Emitir expensas, salir y volver a entrar | Los archivos .txt conservan los datos | Correcto |

## 3. Errores encontrados y corregidos

| Error | Cómo se detectó | Corrección |
|---|---|---|
| Al reabrir, el reclamo volvía a "abierto" en vez de "en curso" | Al comparar el código con el supuesto S9 del análisis | Se corrigió `Reclamo.reabrir()` |
| Los 5 días de reapertura se contaban desde la creación del reclamo y no desde su resolución | Revisión de la regla de negocio | Se agregó el atributo `fechaResolucion` |
| Una prueba esperaba que pagar el capital dejara la expensa saldada | La prueba 13 dio FALLA | Estaba mal la prueba: hay que pagar capital más intereses. Se corrigió y se agregó el caso 14 |
| Los repositorios solo imprimían por pantalla y no guardaban nada | Al revisar el requisito de persistencia | Se implementó la lectura y escritura real con `PrintWriter` y `BufferedReader` |
| Se usaban `IllegalArgumentException` e `IllegalStateException` | El enunciado pide excepciones propias | Se creó la jerarquía que arranca en `BarrioException` |
| Había clases escritas que no se usaban desde ningún lado: `AjustePago`, `Autorizacion_Recurrente`, `Personal_de_seguridad` y `ComparadorReclamoPorFecha` | Buscando desde qué clase se llamaba a cada una | Se agregaron al menú el registro de ajustes (CU9), la autorización de proveedores habituales y el listado de reclamos por antigüedad, y el guardia quedó asociado al registro de fallas |
