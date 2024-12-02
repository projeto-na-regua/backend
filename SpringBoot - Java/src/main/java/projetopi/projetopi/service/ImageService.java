package projetopi.projetopi.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.response.BucketFirebase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class ImageService {

    public String upload(MultipartFile multipartFile, String typeStorage) {
        try {
            String fileName = UUID.randomUUID().toString().concat(this.getExtension(multipartFile.getOriginalFilename()));
            return this.uploadFile(multipartFile.getInputStream(), multipartFile, fileName, typeStorage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Image couldn't upload, Something went wrong");
        }
    }

    private String uploadFile(InputStream inputStream, MultipartFile multipartFile, String fileName, String typeStorage) throws IOException {
        BucketFirebase bucketFirebase = this.defineKeyBucket(typeStorage);
        if (bucketFirebase == null) {
            throw new IllegalArgumentException("Invalid storage type: " + typeStorage);
        }

        BlobId blobId = BlobId.of(bucketFirebase.getName(), fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(multipartFile.getContentType())
                .build();

        // Obter credenciais a partir do caminho configurado nas variáveis de ambiente
        String credentialsPath = System.getenv(bucketFirebase.getKey());
        if (credentialsPath == null) {
            throw new IllegalArgumentException("Firebase credentials not found in environment variables.");
        }

        try (InputStream credentialsStream = new FileInputStream(credentialsPath)) {
            Credentials credentials = GoogleCredentials.fromStream(credentialsStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            // Faz o upload do InputStream diretamente para o Firebase
            storage.create(blobInfo, inputStream);

            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error during image upload");
        }
    }

    private String getExtension(String fileName) {
        if (fileName == null || fileName.isEmpty() || !fileName.contains(".")) {
            return ""; // Retorna uma string vazia se não houver extensão
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String getImgURL(String fileName, String typeStorage) {
        BucketFirebase bucketFirebase = this.defineKeyBucket(typeStorage);
        if (bucketFirebase == null) {
            throw new IllegalArgumentException("Invalid storage type: " + typeStorage);
        }

        try {
            // Obtém o caminho da variável de ambiente para as credenciais
            String credentialsPath = System.getenv(bucketFirebase.getKey());
            if (credentialsPath == null) {
                throw new IllegalArgumentException("Firebase credentials not found in environment variables.");
            }

            try (InputStream inputStream = new FileInputStream(credentialsPath)) {
                Credentials credentials = GoogleCredentials.fromStream(inputStream);
                Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
                Blob blob = storage.get(BlobId.of(bucketFirebase.getName(), fileName));

                if (blob != null && blob.exists()) {
                    String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + bucketFirebase.getName() + "/o/%s?alt=media";
                    return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
                } else {
                    return "File not found";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error while getting image link");
        }
    }

    public BucketFirebase defineKeyBucket(String typeStorage) {
        switch (typeStorage.toLowerCase()) {
            case "barbearia":
                return new BucketFirebase("naregua-upload.appspot.com", "GOOGLE_APPLICATION_CREDENTIALS_NAREGUA_UPLOAD");
            case "usuario":
                return new BucketFirebase("upload-usuarios.appspot.com", "GOOGLE_APPLICATION_CREDENTIALS_UPLOAD_USUARIOS");
            case "comunidade":
                return new BucketFirebase("upload-comunidade.appspot.com", "GOOGLE_APPLICATION_CREDENTIALS_UPLOAD_COMUNIDADE");
            case "galeria":
                return new BucketFirebase("upload-galeria.appspot.com", "GOOGLE_APPLICATION_CREDENTIALS_UPLOAD_GALERIA");
            case "chat":
                return new BucketFirebase("chat-5568f.appspot.com", "GOOGLE_APPLICATION_CREDENTIALS_CHAT");
            default:
                return null; // Retorna null para tipos inválidos
        }
    }
}
