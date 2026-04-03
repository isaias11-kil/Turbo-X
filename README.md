¡Con mucho gusto! Actualizar la documentación es el toque final perfecto para que tu equipo sepa exactamente cómo aprovechar todo el trabajo de configuración que acabas de hacer. 

He reestructurado el `README.md` para colocar la opción de **VS Code con Dev Containers como la principal y más recomendada**, ya que es la que menos problemas de configuración les dará a tus compañeros. También mantuve las instrucciones para NetBeans y la consola por si alguien prefiere esos métodos.

Copia este texto y pégalo en tu archivo `README.md`, guarda los cambios y haz un nuevo *commit* y *push*.

```markdown
# Compilador Turbo X - Fase I (Análisis Léxico) 🚀

Este repositorio contiene la Fase I del proyecto de construcción del compilador para el lenguaje Turbo $X^{\wedge}2$. En esta etapa, el analizador léxico (Scanner) está construido utilizando **Java** y **JFlex**.

---

## 📌 Requisitos Previos Generales
* Clonar este repositorio en tu máquina local.
* **Docker Desktop** instalado y en ejecución (Recomendado).
* IDE de desarrollo (Visual Studio Code o NetBeans).

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

Si prefieres usar NetBeans de forma local, debes asegurarte de tener el **JDK 22** instalado y vincular correctamente la librería de JFlex que se incluye en el repositorio.

**Pasos:**
1. Abre el proyecto en NetBeans.
2. Despliega la carpeta **Libraries**. Si ves `jflex-full-1.9.1.jar` con un error, dale clic derecho y selecciona **Remove**.
3. Haz clic derecho en **Libraries** -> **Add JAR/Folder...**
4. Navega dentro de la carpeta de este proyecto, entra a la carpeta `lib/` y selecciona el archivo `jflex-full-1.9.1.jar`.
5. Ejecuta `GeneradorLexer.java` (**Shift + F6**) para crear la clase `Lexer.java`.
6. Edita el archivo `prueba.txt` con las sentencias de Turbo X a evaluar.
7. Ejecuta `Main.java` (**Shift + F6**) para ver la salida en consola.

---

## 🐳 Opción 3: Ejecución rápida con Docker (Terminal)

Si solo quieres probar el compilador rápidamente sin abrir un IDE, puedes usar la consola de comandos.

Abre tu terminal en la carpeta raíz del proyecto y ejecuta:

**1. Construir la imagen (Solo la primera vez o si el código fuente cambia):**
```bash
docker build -t compilador-turbo-x .
```

**2. Ejecutar el analizador:**
```bash
docker run --rm compilador-turbo-x
```
Esto compilará los archivos, analizará el contenido actual de `prueba.txt`, imprimirá el resultado en consola y destruirá el contenedor al finalizar.

---
**⚠️ Notas Importantes para el Equipo:** * La carpeta compilada (`build/`) y el archivo `Lexer.java` son ignorados por Git de forma intencional para evitar conflictos. **Cada miembro debe generar su propio Lexer localmente** ejecutando `GeneradorLexer.java`.
* **NO modificar manualmente el archivo `Lexer.java`.** Cualquier ajuste en los tokens o expresiones regulares debe hacerse exclusivamente editando el archivo `Lexer.flex`.
```