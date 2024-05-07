package projetopi.projetopi;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class AzureStorageManager {

    @Autowired
    private Environment env;

    private String conectionString;

    private String blobContainerName;

    private String blobName;

    @Autowired // Optionally, you can use constructor injection instead of field injection
    public AzureStorageManager(Environment env) {
        this.env = env; // Optional if you're using constructor injection
        conectionString = env.getProperty("CONECTION_SPRING");
        blobContainerName = env.getProperty("BLOB_CONTAINER");
        blobName = env.getProperty("BLOB");
    }


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

