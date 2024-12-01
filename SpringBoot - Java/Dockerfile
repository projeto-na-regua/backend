# Etapa de build
FROM maven:3-openjdk-17 AS builder
LABEL author="melissaneves"

# Define o diretório de trabalho para a etapa de construção
WORKDIR /build

# Copia o conteúdo do projeto para o contêiner
COPY .. .

# Executa o Maven para construir o projeto
RUN mvn clean package -DskipTests -Dcheckstyle.skip=true

# Etapa de execução
FROM openjdk:17-slim

# Define o diretório de trabalho para a aplicação
WORKDIR /app

# Copia o JAR gerado na etapa de construção para o contêiner final
COPY --from=builder /build/target/*.jar /app/app.jar

# Copia os arquivos de credenciais do Firebase para o contêiner final
COPY --from=builder /build/src/main/resources/*.json /app/config/

# Define o caminho base das credenciais como variável de ambiente (opcional)
ENV CREDENTIALS_PATH=/app/config/

# Define o comando padrão para executar o aplicativo
CMD ["java", "-jar", "app.jar"]

