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
        String containerName = "upload";
        BlobContainerClient containerClient = azureStorageManager.setCliente(containerName).getContainerClient();
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);
        return outputStream.toByteArray();
    }

    public List<byte[]> getBlobArray(List<String> blobNames) throws IOException {
        String containerName = "upload";
        BlobContainerClient containerClient = azureStorageManager.setCliente(containerName).getContainerClient();
        List<byte[]> imageBytesList = new ArrayList<>();
        for (String blobName : blobNames) {
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blobClient.download(outputStream);
            imageBytesList.add(outputStream.toByteArray());
        }
        return imageBytesList;
    }

    public String getBlobUrl(String blobName) {
        String containerName = "upload";
        BlobContainerClient containerClient = azureStorageManager.setCliente(containerName).getContainerClient();
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        return blobClient.getBlobUrl();
    }
}
