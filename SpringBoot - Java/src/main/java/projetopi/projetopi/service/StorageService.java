package projetopi.projetopi.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Service("azureStorageService")
public class StorageService {
    @Autowired
    private final StorageManager azureStorageManager;

    public StorageService(StorageManager azureStorageManager) {
        this.azureStorageManager = azureStorageManager;
    }


    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        String imageUrl = azureStorageManager.uploadFileToBlobStorage(fileName, tempFile.getAbsolutePath());
        return imageUrl;
    }

    public byte[] getBlob(String blobName) throws IOException {
        // Nome do seu container de armazenamento na Azure
        String containerName = "upload";

        // Recupera uma referência para o container de blobs
        BlobContainerClient containerClient = azureStorageManager.setCliente(containerName).getContainerClient();

        // Recupera uma referência para o blob
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        // Faz o download do conteúdo do blob
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);

        // Retorna o conteúdo do blob como um array de bytes
        return outputStream.toByteArray();
    }

    public List<byte[]> getBlobArray(List<String> blobNames) throws IOException {
        // Nome do seu container de armazenamento na Azure
        String containerName = "upload";

        // Recupera uma referência para o container de blobs
        BlobContainerClient containerClient = azureStorageManager.setCliente(containerName).getContainerClient();

        List<byte[]> imageBytesList = new ArrayList<>();

        // Itera sobre a lista de nomes de blobs
        for (String blobName : blobNames) {
            // Recupera uma referência para o blob
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            // Faz o download do conteúdo do blob
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blobClient.download(outputStream);

            // Adiciona o conteúdo do blob como um array de bytes à lista
            imageBytesList.add(outputStream.toByteArray());
        }

        // Retorna a lista de arrays de bytes das imagens
        return imageBytesList;
    }

}
