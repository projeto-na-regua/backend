#!/bin/bash

# Lista de arquivos a serem removidos do histórico
files=(
  "SpringBoot - Java/src/main/resources/chat-5568f-firebase-adminsdk-unsxe-8a450dcc9a.json"
  "SpringBoot - Java/src/main/resources/naregua-upload-firebase-adminsdk-9hly7-6fe030a9b8.json"
  "SpringBoot - Java/src/main/resources/upload-comunidade-firebase-adminsdk-sxo3w-de42cb0bae.json"
  "SpringBoot - Java/src/main/resources/upload-galeria-firebase-adminsdk-d6ewd-1312a87bcb.json"
  "SpringBoot - Java/src/main/resources/upload-usuarios-firebase-adminsdk-6r1vz-565bdbf21c.json"
)

# Loop para remover cada arquivo do histórico
for file in "${files[@]}"; do
  echo "Removendo o arquivo: $file"
  git filter-branch --force --index-filter "git rm --cached --ignore-unmatch '$file'" --prune-empty --tag-name-filter cat -- --all
done

# Push forçado para o repositório remoto
echo "Fazendo push forçado para o repositório remoto..."
git push origin --force --all

echo "Todos os arquivos sensíveis foram removidos do histórico."
