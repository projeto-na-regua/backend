package projetopi.projetopi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

@Service("s3StorageManager")
public class StorageManager {

    @Autowired
    private AmazonS3 amazonS3;

    private final String bucketName = "nareguabucket"; // Substitua pelo nome do seu bucket S3


    public String uploadFileToBlobStorage(String fileName, String filePath) {
        String uniqueFileName = UUID.randomUUID().toString() + "-" + fileName;

        // Cria um objeto de metadados se necessário (opcional)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(new File(filePath).length()); // Define o tamanho do arquivo

        // Cria um PutObjectRequest e faz o upload
        PutObjectRequest request = new PutObjectRequest(bucketName, uniqueFileName, new File(filePath));
        amazonS3.putObject(request);

        // Retorna o nome único do arquivo
        return uniqueFileName;
    }

    // Não há necessidade de um método setCliente para o S3; as operações são feitas através de AmazonS3.
}
