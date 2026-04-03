# 1. Usar la imagen oficial de Java (JDK 22)
FROM eclipse-temurin:22-jdk

# 2. Establecer la carpeta de trabajo dentro del contenedor
WORKDIR /app

# 3. Copiar todo el código de tu proyecto al contenedor
COPY . /app

# 4. Compilar los archivos Java manualmente (para Linux)
# Le decimos a Java dónde está JFlex y dónde guardar los compilados
RUN javac -cp "Libraries/jflex-full-1.9.1.jar" -d build src/codigo/*.java

# 5. Comando que se ejecutará por defecto al iniciar el contenedor
# Ejecuta el Main pasándole la ruta de la librería y de los compilados
CMD ["java", "-cp", "build:Libraries/jflex-full-1.9.1.jar", "codigo.Main"]