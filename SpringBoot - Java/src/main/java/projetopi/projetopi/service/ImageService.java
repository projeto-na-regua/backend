package projetopi.projetopi.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.response.BucketFirebase;

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
            return "Image couldn't upload, Something went wrong";
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

        // Obtém credenciais para o Firebase
        try (InputStream credentialsStream = ImageService.class.getClassLoader().getResourceAsStream(bucketFirebase.getKey())) {
            Credentials credentials = GoogleCredentials.fromStream(credentialsStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            // Faz o upload do InputStream diretamente para o Firebase
            storage.create(blobInfo, inputStream.readAllBytes());

//            String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + bucketFirebase.getName() + "/o/%s?alt=media";
//            return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
              return fileName;
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
            return "Invalid storage type: " + typeStorage;
        }

        try (InputStream inputStream = ImageService.class.getClassLoader().getResourceAsStream(bucketFirebase.getKey())) {
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            Blob blob = storage.get(BlobId.of(bucketFirebase.getName(), fileName));

            if (blob != null && blob.exists()) {
                String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + bucketFirebase.getName() + "/o/%s?alt=media";
                return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            } else {
                return "File not found";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error while getting image link";
        }
    }

    public BucketFirebase defineKeyBucket(String typeStorage) {
        switch (typeStorage.toLowerCase()) {
            case "barbearia":
                return new BucketFirebase("naregua-upload.appspot.com", "naregua-upload-firebase-adminsdk-9hly7-6fe030a9b8.json");
            case "usuario":
                return new BucketFirebase("upload-usuarios.appspot.com", "upload-usuarios-firebase-adminsdk-6r1vz-565bdbf21c.json");
            case "comunidade":
                return new BucketFirebase("upload-comunidade.appspot.com", "upload-comunidade-firebase-adminsdk-sxo3w-de42cb0bae.json");
            case "galeria":
                return new BucketFirebase("upload-galeria.appspot.com", "upload-galeria-firebase-adminsdk-d6ewd-1312a87bcb.json");
            case "chat":
                return new BucketFirebase("chat-5568f.appspot.com", "chat-5568f-firebase-adminsdk-unsxe-8a450dcc9a.json");
            default:
                return null; // Retorna null para tipos inválidos
        }
    }
}
