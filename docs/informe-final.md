# Informe final - Etapa 2

**Propuesta A: gestión de un barrio cerrado "Los Aromos"**
Programación Orientada a Objetos - UADE, 2° cuatrimestre 2026
Integrantes: Rodrigo Peña Fernandez y Nahuel Tomás Caballero

---

## 1. Cambios respecto del diseño original

El diagrama de clases final de la Etapa 1 se respetó casi por completo. Estos son los
cambios que tuvimos que hacer al codificar:

### 1.1 Se agregaron dos clases de servicio

En el diagrama estaban `GestorReclamo`, `GestorExpensas` y `GeneradorReportes`, pero no
había ninguna clase que se ocupara de los equipos ni de las visitas. Al escribir el código
vimos que esa lógica terminaba toda dentro del `Main`, así que agregamos:

- **`GestorMantenimiento`**: registra las fallas de los equipos, genera las órdenes y
  controla que no se abra una segunda orden sobre un equipo que ya tiene una (supuesto S15).
- **`GestorAccesos`**: valida las autorizaciones y registra los ingresos y egresos.

El motivo es el mismo por el que ya existían los otros gestores: no cargar al `Administrador`
con responsabilidades que no le corresponden.

### 1.2 Se corrigió la dirección de los repositorios

En el diagrama final, `Lote`, `GestorExpensas` y `GestorReclamo` aparecían **implementando**
las interfaces de repositorio. Eso estaba mal: las clases que implementan los repositorios
son las `ArchivoRepositorio…`, y los gestores lo que hacen es **usarlas**. En el código quedó
como corresponde, con los gestores recibiendo la interfaz por el constructor.

### 1.3 Los comentarios del reclamo

El diagrama tenía a la vez el atributo `comentarios: List<String>` dentro de `Reclamo` y una
clase `Comentario` aparte. Nos quedamos con la clase, porque el enunciado pide guardar el
autor y la fecha de cada comentario, y eso con una lista de textos no se puede.

### 1.4 Atributos agregados

- `Reclamo.fechaResolucion`: hacía falta para contar los 5 días de reapertura desde que el
  reclamo se resolvió, y no desde que se creó.
- `Expensa.saldo` e `Expensa.interesesPendientes`: el diagrama solo tenía `importe`, pero para
  imputar pagos parciales hay que llevar por separado lo que falta de capital y de intereses.

### 1.5 El metodo prioridadDeOrden recibe un parametro

En el diagrama, `Equipo` no tenia metodos. Al codificar necesitabamos que cada tipo de equipo
decidiera con que prioridad se abre su orden, asi que agregamos `prioridadDeOrden(int fallasDelSector)`:
la camara devuelve siempre "alta" y la luminaria mira cuantas hay falladas en su sector.
Le pasamos ese numero por parametro para que el equipo no tenga que conocer al gestor.

### 1.6 Nombres

Dejamos los nombres de clase tal como están en el diagrama, incluso los que llevan guión bajo
(`Personal_de_Mantenimiento`, `Orden_Mantenimiento`, `Autorizacion_Unica`), para que el código
y el diagrama se correspondan uno a uno.

---

## 2. Dónde se ven los patrones en el código

### Principios SOLID

| Principio | Dónde está en el código |
|---|---|
| **SRP** (responsabilidad única) | `GestorReclamo`, `GestorExpensas`, `GestorMantenimiento`, `GestorAccesos` y `GeneradorReportes`. Cada uno se ocupa de un tema, en vez de que el `Administrador` haga todo. |
| **OCP** (abierto/cerrado) | La interfaz `MedioPago` con `PagoEfectivo`, `PagoTarjeta` y `PagoTransferencia`. Para agregar un medio de pago nuevo alcanza con crear una clase más: `Pago` no se toca. |
| **DIP** (inversión de dependencias) | `GestorExpensas` y `GestorReclamo` reciben `RepositorioExpensas`, `RepositorioLotes` y `RepositorioReclamo` por el constructor. No conocen a las clases `ArchivoRepositorio…`, así que se podría cambiar la forma de guardar sin tocarlos. |

### Patrones GRASP

| Patrón | Dónde está en el código |
|---|---|
| **Fabricación pura** | Los gestores y el `GeneradorReportes`. No representan nada del mundo real del barrio, pero ordenan el sistema. |
| **Polimorfismo** | `Equipo.prioridadDeOrden()`, que `CamaraSeguridad` y `Luminaria` resuelven distinto; `MedioPago.procesarPago()`; y `Autorizacion.esValida()` en sus dos subclases. En ningún caso hace falta preguntar de qué tipo es el objeto. |
| **Indirección** | Las interfaces de repositorio se ponen en el medio entre los gestores y las clases que leen y escriben los archivos. |
| **Variaciones protegidas** | Los dos puntos que más pueden cambiar (los medios de pago y la forma de guardar los datos) quedaron detrás de interfaces, así un cambio ahí no se propaga al resto. |

---

## 3. Requisitos técnicos

| Requisito | Dónde se cumple |
|---|---|
| Organización en paquetes | `modelo`, `gestores`, `repositorios`, `excepciones`, `comparadores`, `pruebas` |
| Herencia y sobreescritura | `Persona` → `Titular`, `Residente`, `Personal` → `Administrador`, `Personal_de_seguridad`, `Personal_de_Mantenimiento`. También `Equipo` → `CamaraSeguridad`, `Luminaria`, y `Autorizacion` → sus dos subclases |
| Clases abstractas e interfaces | Abstractas: `Persona`, `Personal`, `Equipo`, `Autorizacion`. Interfaces: `MedioPago` y los tres repositorios |
| Encapsulamiento | Atributos privados, `protected` en las clases abstractas, y acceso por métodos |
| Colecciones | `ArrayList` y `List` en todo el modelo |
| Ordenamiento natural | `Reclamo implements Comparable`: ordena por prioridad y, si empatan, por fecha |
| Ordenamiento personalizado | `ComparadorReclamoPorFecha` y `ComparadorLotePorDeuda` |
| Excepciones propias | `BarrioException` y sus cuatro hijas, lanzadas con `throw` y declaradas con `throws` |
| try / catch / finally | En los menús del `Main` y en los repositorios, donde el `finally` cierra el archivo |
| Entrada y salida | `PrintWriter`, `FileWriter`, `BufferedReader` y `FileReader` en los `ArchivoRepositorio…` |
| Interfaz | Menú por consola con `Scanner`, con validación de lo que se escribe |
| Pruebas | 53 casos en `/test`, detallados en el plan de pruebas |

---

## 4. Dificultades encontradas

- **Ponernos de acuerdo entre el diagrama y el código.** Varias veces arrancamos a escribir
  una clase y nos dimos cuenta de que el diagrama decía otra cosa, o que le faltaba un
  atributo. Lo que nos sirvió fue volver al análisis de la Etapa 1 y revisar los supuestos
  que habíamos escrito, en vez de decidir sobre la marcha.

- **La imputación de los pagos.** Fue la regla más difícil, porque hay que aplicar el importe
  primero a los intereses y después al capital, y además pasar lo que sobra a la expensa
  siguiente. Lo resolvimos haciendo que `Expensa.aplicarImporte()` devuelva el sobrante.

- **Hacer las pruebas sin una librería.** Como no vimos JUnit en la cursada, armamos una clase
  `Verificador` que compara lo esperado con lo obtenido e imprime el resultado. Una de las
  pruebas dio error y, al revisarla, el que estaba mal era lo que esperábamos nosotros, no el
  código: habíamos olvidado que al capital hay que sumarle los intereses.

- **Trabajar los dos sobre el mismo repositorio.** Al principio cada uno escribía en su
  carpeta y después copiaba los archivos, lo que generó problemas con los paquetes. Terminamos
  usando una rama por tema y pull requests, que es lo que pide la consigna.

---

## 5. Distribución de tareas

> COMPLETAR entre los dos antes de entregar: poner quién hizo cada cosa de verdad.
> Esto se compara con los commits del repositorio, así que tiene que coincidir.

| Tarea | Responsable |
|---|---|
| Análisis del enunciado y supuestos | |
| Diagrama de casos de uso y fichas | |
| Diagrama de clases inicial | |
| Diagramas de secuencia | |
| SOLID y GRASP | |
| Diagrama de clases final | |
| Clases del modelo | |
| Gestores y menú | |
| Repositorios y persistencia | |
| Pruebas y plan de pruebas | |
| Documentación | |

---

## 6. Reflexión del grupo

> COMPLETAR con lo que piensan ustedes. Algunas cosas que se pueden contar:
> qué les resultó más difícil, qué harían distinto si lo empezaran de nuevo,
> para qué les sirvió hacer primero el diagrama antes de escribir el código,
> y qué aprendieron de trabajar con Git entre dos personas.
