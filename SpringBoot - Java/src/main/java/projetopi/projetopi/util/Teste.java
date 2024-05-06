package projetopi.projetopi.util;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;

public class Teste {


    private String conectionString = "DefaultEndpointsProtocol=https;AccountName=upload0naregua;AccountKey=pjZHirndf8IoR10ThS02jU+yC7JHN55QtIdPkv4XnK+SfDD8MhLf/2tZgwt1/51vlhivKMzsXwr4+AStG7Jsiw==;EndpointSuffix=core.windows.net";

    private String blobContainerName = "upload";

    private String blobName;
    BlobClient blobClient = new BlobClientBuilder()
            .connectionString(conectionString)
            .containerName(blobContainerName)
            .blobName(blobName).buildClient();



}
