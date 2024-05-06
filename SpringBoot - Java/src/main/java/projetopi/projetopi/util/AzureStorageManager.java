package projetopi.projetopi.util;

import com.azure.resourcemanager.storage.models.StorageAccountMicrosoftEndpoints;
import com.azure.spring.cloud.autoconfigure.implementation.storage.AzureStorageConfiguration;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;


@Component
public class AzureStorageManager {

    private String conectionString = "DefaultEndpointsProtocol=https;AccountName=upload0naregua;AccountKey=pjZHirndf8IoR10ThS02jU+yC7JHN55QtIdPkv4XnK+SfDD8MhLf/2tZgwt1/51vlhivKMzsXwr4+AStG7Jsiw==;EndpointSuffix=core.windows.net";

    private String blobContainerName = "upload";

    private String blobName = "upload0naregua";


    public String uploadFileToBlobStorage(String fileName, String filePath) {
        String uniqueFileName = UUID.randomUUID().toString() + "-" + fileName;
        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(conectionString)
                .containerName(blobContainerName)
                .blobName(uniqueFileName).buildClient();

        blobClient.uploadFromFile(filePath);
        return uniqueFileName;
    }

    public BlobClient setCliente(String blobContainerName) {

        BlobClient blobClient = new BlobClientBuilder()
                .connectionString(conectionString)
                .containerName(blobContainerName)
                .blobName(blobName)
                .buildClient();

        return blobClient;
    }
}

