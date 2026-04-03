# Compilador Turbo X - Fase I (Análisis Léxico) 🚀

Este repositorio contiene la Fase I del proyecto de construcción del compilador para el lenguaje Turbo $X^{\wedge}2$. En esta etapa, el analizador léxico (Scanner) está construido utilizando **Java** y **JFlex**.

## 📌 Requisitos Previos
* **Java JDK 22** (Recomendado para evitar conflictos de versión).
* IDE de desarrollo (NetBeans, IntelliJ o Eclipse).
* **Docker** (Opcional, para ejecución en contenedor aislado).

---

## 💻 Opción 1: Cómo ejecutar el proyecto en NetBeans (IDE)

Al clonar este repositorio, la carpeta compilada (`build`) y los archivos generados automáticamente no se incluyen. Sigue estos pasos para que el proyecto funcione correctamente en tu máquina:

### 1. Vincular la librería JFlex
Dado que las rutas absolutas cambian entre computadoras, debes re-vincular JFlex a la carpeta local del proyecto:
1. En NetBeans, despliega el proyecto y ve a la carpeta **Libraries**.
2. Si ves `jflex-full-1.9.1.jar` con un error, dale clic derecho y selecciona **Remove**.
3. Haz clic derecho en **Libraries** -> **Add JAR/Folder...**
4. Navega dentro de este mismo proyecto descargado, entra a la carpeta `lib/` y selecciona el archivo `jflex-full-1.9.1.jar`.

### 2. Orden de Ejecución
Para que el compilador funcione, debes generar el Lexer antes de correr el Main:
1. Abre el archivo `GeneradorLexer.java` y ejecútalo (**Shift + F6**). Esto leerá las reglas de `Lexer.flex` y creará la clase `Lexer.java`.
2. Escribe o modifica el código de prueba en el archivo `prueba.txt`.
3. Abre el archivo `Main.java` y ejecútalo (**Shift + F6**). Verás en la consola la lista de tokens reconocidos y cualquier error léxico detectado.

---

## 🐳 Opción 2: Cómo ejecutar usando Docker (Terminal)

Si prefieres no configurar el IDE o tienes problemas con las versiones de Java, puedes correr el analizador léxico usando Docker. Esto creará un entorno virtual con todo lo necesario.

Abre tu terminal en la carpeta raíz del proyecto y ejecuta:

**1. Construir la imagen (Solo la primera vez o si modificas el código):**
```bash
docker build -t compilador-turbo-x .
```

**2. Ejecutar el analizador:**
```bash
docker run --rm compilador-turbo-x
```
Esto compilará los archivos, leerá las instrucciones de `prueba.txt`, imprimirá los tokens en consola y destruirá el contenedor al finalizar para no ocupar espacio.

---
**Nota para el equipo:** No modificar manualmente el archivo `Lexer.java`. Cualquier cambio en los tokens o expresiones regulares debe hacerse únicamente dentro de `Lexer.flex`.
