# 1. Usar la imagen oficial de Java (JDK 22)
FROM eclipse-temurin:22-jdk

# 2. Establecer la carpeta de trabajo dentro del contenedor
WORKDIR /app

# 3. Copiar todo el código de tu proyecto al contenedor
COPY . /app

# 4. Compilar los archivos Java manualmente (para Linux)
RUN javac -cp "lib/*" -d build src/codigo/*.java src/app/*.java

# 5. Comando que se ejecutará por defecto al iniciar el contenedor
CMD ["java", "-cp", "build:lib/*", "codigo.Main"]