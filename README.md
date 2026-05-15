
# Compilador Turbo X - Fase I (Análisis Léxico) 🚀

Este repositorio contiene el proyecto de construcción del compilador para el lenguaje Turbo $X^{\wedge}2$. El analizador léxico (Scanner) está construido utilizando **Java** y **JFlex**; el análisis sintáctico y semántico utiliza **Java CUP**. Incluye además el **motor de ejecución visual (Rol 3)** con gráficas mediante **JFreeChart**.

---

## 📌 Requisitos Previos Generales
* Clonar este repositorio en tu máquina local.
* **JDK** (21 o 22 recomendado) instalado y configurado en el `PATH`, o bien **Docker Desktop** (opcional).
* IDE de desarrollo (Visual Studio Code, Cursor o NetBeans).

---

## 📊 Gráficas con `graficar` (Rol 3 — JFreeChart)

Cuando el código **no tiene errores** léxicos, sintácticos ni semánticos, la instrucción **`graficar`** abre una ventana con una gráfica de **barras** o de **líneas** según los datos de la variable indicada.

### Sintaxis

```text
graficar(nombreVariable);
```

La variable debe existir y su **valor** se interpreta así:

| Tipo     | Comportamiento |
|----------|----------------|
| **Entero** / **Real** | Un solo valor → una categoría en el gráfico. |
| **Cadena** | Lista de números separados por **comas** → por defecto **gráfica de barras**. Ejemplo: `"12, 18, 7, 22"`. |
| **Cadena** (modo líneas) | Si el texto **empieza** por `LINEAS:` (mayúsculas/minúsculas y acentos equivalentes, p. ej. `Líneas:`), el resto tras los dos puntos es la lista numérica y se usa **gráfica de líneas**. Ejemplo: `"LINEAS:5, 9, 4, 11, 8"`. |
| **Cadena** (modo barras explícito) | Prefijo opcional `BARRAS:` antes de la lista. |

### Ejemplo completo

En el repositorio está el archivo **`Ejemplos para prueba/rol3_graficas.txt`**, que puedes copiar en la interfaz o adaptar:

```text
INICIO
    Cadena ventas <- "12, 18, 7, 22";
    graficar(ventas);

    Cadena tendencia <- "LINEAS:5, 9, 4, 11, 8";
    graficar(tendencia);

    Entero meta <- 100;
    graficar(meta);
FIN
```

Cada `graficar` puede abrir **una ventana** adicional (una por instrucción ejecutada en el análisis).

### Cómo probarlo con la interfaz gráfica

1. Compila incluyendo **todos** los JAR de `lib/` (JFlex, CUP runtime, **JFreeChart**):

   **Windows (PowerShell), desde la raíz del proyecto:**

   ```powershell
   mkdir build -Force
   javac -encoding UTF-8 -cp "lib/*" -d build (Get-ChildItem src/codigo -Filter *.java).FullName (Get-ChildItem src/app -Filter *.java).FullName
   java -cp "build;lib/*" app.InterfazGraficaModerna
   ```

   **Linux / macOS:**

   ```bash
   mkdir -p build
   javac -encoding UTF-8 -cp "lib/*" -d build src/codigo/*.java src/app/*.java
   java -cp "build:lib/*" app.InterfazGraficaModerna
   ```

2. Pega un programa Turbo X válido (por ejemplo el de `rol3_graficas.txt`) y pulsa **Analizar**. Si no hay errores, deberían mostrarse las ventanas con las gráficas.

### Archivos relevantes del código

* `src/codigo/MotorEjecucion.java` — punto de entrada del motor al ejecutar `graficar`.
* `src/codigo/ServicioGraficasJFreeChart.java` — integración con **JFreeChart** (dataset, ventana Swing).
* `src/codigo/Sintax.cup` — regla `GraficarInstr` que invoca el motor tras validar la variable.
* `lib/jfreechart-1.5.4.jar` — dependencia externa de gráficas.

---

## 🌟 Opción 1: VS Code + Dev Containers (Recomendado)
Esta es la forma más segura de trabajar en equipo. Utiliza un contenedor de Docker integrado directamente en el IDE, garantizando que todos tengamos la misma versión de Java y las mismas extensiones sin configurar nada localmente.

**Requisitos extra:** Instalar la extensión "Dev Containers" en VS Code.

**Pasos:**
1. Abre la carpeta del proyecto en VS Code.
2. En la esquina inferior derecha aparecerá una notificación. Haz clic en el botón **"Reopen in Container"** (Reabrir en contenedor).
3. VS Code construirá el entorno automáticamente (tomará unos minutos la primera vez).
4. Una vez cargado, abre el archivo `src/codigo/GeneradorLexer.java` y haz clic en el botón **Run** (o ejecuta el archivo) para crear el analizador.
5. Edita el archivo `prueba.txt` con el código que desees analizar.
6. Abre `src/codigo/Main.java` y haz clic en **Run**. La terminal integrada mostrará los tokens procesados.

---

## ☕ Opción 2: Cómo ejecutar el proyecto en NetBeans

Si prefieres usar NetBeans de forma local, debes asegurarte de tener el **JDK 22** instalado y vincular correctamente las librerías que se incluyen en el repositorio.

**Pasos:**
1. Abre el proyecto en NetBeans.
2. Despliega la carpeta **Libraries**. Si algún JAR muestra error, elimínalo y vuelve a añadirlo.
3. Haz clic derecho en **Libraries** → **Add JAR/Folder...**
4. Navega a la carpeta `lib/` del proyecto y añade **todos** los archivos `.jar` (JFlex, CUP, CUP runtime, **JFreeChart**).
5. Ejecuta `GeneradorLexer.java` o `Principal.java` (**Shift + F6**) cuando necesites regenerar `Lexer.java` / `LexerCup.java` / `Sintax.java` desde los `.flex` / `.cup`.
6. Para la interfaz con tabla de símbolos y gráficas, ejecuta **`app.InterfazGraficaModerna`** como clase principal.

---

## 🐳 Opción 3: Ejecución rápida con Docker (Terminal)

Si solo quieres probar el compilador rápidamente sin abrir un IDE, puedes usar la consola de comandos.

Abre tu terminal en la carpeta raíz del proyecto y ejecuta:

**1. Construir la imagen (solo la primera vez o si el código fuente cambia):**

```bash
docker build -t compilador-turbo-x .
```

**2. Ejecutar:**

```bash
docker run --rm compilador-turbo-x
```

Esto compilará los archivos con el classpath `lib/*` y ejecutará la clase por defecto del contenedor. **Nota:** en un contenedor sin entorno gráfico las ventanas de **JFreeChart** no se mostrarán; para probar `graficar` conviene ejecutar **`InterfazGraficaModerna`** en tu máquina local.

---

## ⚠️ Notas importantes para el equipo

* La carpeta compilada (`build/`) y, según configuración, el archivo `Lexer.java` pueden ignorarse en Git para evitar conflictos. **Cada miembro debe generar su propio analizador localmente** cuando cambien `Lexer.flex` / `LexerCup.flex` / `Sintax.cup` (por ejemplo ejecutando `GeneradorLexer.java` o `Principal.java`).
* **No modificar manualmente** `Lexer.java` / `LexerCup.java` / `Sintax.java` salvo que sepas que son salidas regeneradas; el código fuente de la gramática y del lexer está en los archivos **`.flex`** y **`.cup`**.
