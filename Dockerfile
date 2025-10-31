# Etapa 1: usar o Java 17 para compilar o projeto
FROM eclipse-temurin:17-jdk AS builder

# Definir a pasta de trabalho dentro do container
WORKDIR /app

# Copiar tudo do seu projeto
COPY . .

# Rodar o Maven para gerar o .jar (sem rodar testes)
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Etapa 2: imagem final mais leve
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiar o .jar gerado da etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Render precisa saber a porta da aplicação
ENV PORT=8080
EXPOSE 8080

# Comando para iniciar a API
CMD ["java", "-jar", "app.jar"]
