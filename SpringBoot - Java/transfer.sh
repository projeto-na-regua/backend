#!/bin/bash

SERVER_IP="44.202.27.20"

# Execute o Maven para limpar e compilar o projeto
mvn clean install

# Verifique se o comando do Maven foi bem-sucedido
if [ $? -eq 0 ]; then
  echo "Build bem-sucedido, iniciando a transferência do arquivo..."
  
  # Comando SCP para transferir o arquivo .jar para o servidor remoto
  scp -i "C:/GitHub/provisionamento/id_rsa.pem" "C:/GitHub/backend/SpringBoot - Java/target/projetopi-0.0.1-SNAPSHOT.jar" ubuntu@$SERVER_IP:/home/ubuntu
  
  # Verifique se a transferência foi bem-sucedida
  if [ $? -eq 0 ]; then
    echo "Arquivo transferido com sucesso!"
  else
    echo "Erro ao transferir o arquivo."
  fi
else
  echo "Erro na construção do projeto. A transferência não será realizada."
fi


