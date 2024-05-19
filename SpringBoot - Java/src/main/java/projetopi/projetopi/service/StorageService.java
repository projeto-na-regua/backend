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

}
