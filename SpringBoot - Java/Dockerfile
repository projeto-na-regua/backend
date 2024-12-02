# Etapa de build
FROM maven:3-openjdk-17 AS builder
LABEL author="melissaneves"

# Define o diretório de trabalho para a etapa de construção
WORKDIR /build

# Copia o conteúdo do projeto para o contêiner
COPY . .

# Executa o Maven para construir o projeto
RUN mvn clean package -DskipTests -Dcheckstyle.skip=true

# Verifique os arquivos dentro de /build/target/classes
RUN ls -R /build/target/classes/

# Etapa de execução
FROM openjdk:17-slim

# Define o diretório de trabalho para a aplicação
WORKDIR /app

# Cria o diretório /app/config se não existir
RUN mkdir -p /app/config

# Copia o JAR gerado na etapa de construção para o contêiner final
COPY --from=builder /build/target/*.jar /app/app.jar

# Copia os arquivos de credenciais do Firebase da pasta de build para o contêiner
COPY --from=builder /build/target/classes/*.json /app/config/

# Define o caminho base das credenciais como variável de ambiente (opcional)
# Define as variáveis de ambiente para apontar para os arquivos de credenciais do Firebase
ENV GOOGLE_APPLICATION_CREDENTIALS_CHAT="/app/config/chat-5568f-firebase-adminsdk-unsxe-21134a8356.json"
ENV GOOGLE_APPLICATION_CREDENTIALS_NAREGUA_UPLOAD="/app/config/naregua-upload-firebase-adminsdk-9hly7-454496d7de.json"
ENV GOOGLE_APPLICATION_CREDENTIALS_UPLOAD_COMUNIDADE="/app/config/upload-comunidade-firebase-adminsdk-sxo3w-56c4ed2507.json"
ENV GOOGLE_APPLICATION_CREDENTIALS_UPLOAD_GALERIA="/app/config/upload-galeria-firebase-adminsdk-d6ewd-34985e2190.json"
ENV GOOGLE_APPLICATION_CREDENTIALS_UPLOAD_USUARIOS="/app/config/upload-usuarios-firebase-adminsdk-6r1vz-cfc61531f3.json"


# Define o comando padrão para executar o aplicativo
CMD ["java", "-jar", "app.jar"]
