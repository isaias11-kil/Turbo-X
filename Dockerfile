# 1. Usar una imagen oficial de Java (JDK 17 es un estándar muy estable)
FROM openjdk:17-jdk-slim

# 2. Instalar la herramienta "ant" (NetBeans la usa para compilar con su archivo build.xml)
RUN apt-get update && apt-get install -y ant && rm -rf /var/lib/apt/lists/*

# 3. Crear una carpeta dentro del contenedor para el proyecto
WORKDIR /app

# 4. Copiar todo el código de tu proyecto al contenedor
COPY . /app

# 5. Abrir la terminal interactiva por defecto al iniciar
CMD ["/bin/bash"]