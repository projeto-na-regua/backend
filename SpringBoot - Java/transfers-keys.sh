#!/bin/bash

# Configuração
SERVER_IP="18.233.99.97"
KEY_PATH="C:/GitHub/provisionamento/id_rsa.pem"
BASE_PATH="C:/GitHub/backend/SpringBoot - Java/target/classes"
REMOTE_PATH="/home/ubuntu"

# Lista de arquivos a serem transferidos
FILES=(
  "chat-5568f-firebase-adminsdk-unsxe-21134a8356.json"
  "naregua-upload-firebase-adminsdk-9hly7-454496d7de.json"
  "upload-comunidade-firebase-adminsdk-sxo3w-56c4ed2507.json"
  "upload-galeria-firebase-adminsdk-d6ewd-34985e2190.json"
  "upload-usuarios-firebase-adminsdk-6r1vz-cfc61531f3.json"
)

echo "Iniciando a construção do projeto..."

# Adicione o comando de construção do projeto aqui, se necessário
# Por exemplo: mvn clean install ou outro comando apropriado
BUILD_SUCCESS=0  # Supondo sucesso para simulação
# Se o comando de build for necessário, atualize BUILD_SUCCESS com o $? do comando
# Exemplo: mvn clean install && BUILD_SUCCESS=$? || BUILD_SUCCESS=$?

if [ $BUILD_SUCCESS -eq 0 ]; then
  echo "Build bem-sucedido, iniciando a transferência dos arquivos..."
  
  # Transferindo arquivos
  for file in "${FILES[@]}"; do
    echo "Transferindo $file..."
    scp -i "$KEY_PATH" "$BASE_PATH/$file" ubuntu@$SERVER_IP:$REMOTE_PATH
    if [ $? -ne 0 ]; then
      echo "Erro ao transferir o arquivo: $file"
      exit 1  # Sai do script em caso de erro
    fi
  done
  
  echo "Todos os arquivos foram transferidos com sucesso!"
else
  echo "Erro na construção do projeto. A transferência não será realizada."
  exit 1
fi
