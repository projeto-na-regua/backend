package projetopi.projetopi.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.dto.response.BucketFirebase;

@Service
public class ImageService {
    public ImageService() {
    }

    public String upload(MultipartFile multipartFile, String typeStorage) {
        try {
            String fileName = UUID.randomUUID().toString().concat(this.getExtension(multipartFile.getOriginalFilename()));
            return this.uploadFile(multipartFile.getInputStream(), multipartFile, fileName, typeStorage);
        } catch (Exception var4) {
            Exception e = var4;
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
    }

    private String uploadFile(InputStream inputStream, MultipartFile multipartFile, String fileName, String typeStorage) throws IOException {
        BucketFirebase bucketFirebase = this.defineKeyBucket(typeStorage);
        if (bucketFirebase == null) {
            throw new IllegalArgumentException("Invalid storage type: " + typeStorage);
        } else {
            BlobId blobId = BlobId.of(bucketFirebase.getName(), fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build();
            InputStream credentialsStream = ImageService.class.getClassLoader().getResourceAsStream(bucketFirebase.getKey());

            String var11;
            try {
                Credentials credentials = GoogleCredentials.fromStream(credentialsStream);
                Storage storage = (Storage)((StorageOptions.Builder)StorageOptions.newBuilder().setCredentials(credentials)).build().getService();
                storage.create(blobInfo, inputStream.readAllBytes(), new Storage.BlobTargetOption[0]);
                var11 = fileName;
            } catch (Throwable var13) {
                if (credentialsStream != null) {
                    try {
                        credentialsStream.close();
                    } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                    }
                }

                throw var13;
            }

            if (credentialsStream != null) {
                credentialsStream.close();
            }

            return var11;
        }
    }

    private String getExtension(String fileName) {
        return fileName != null && !fileName.isEmpty() && fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }

    public String getImgURL(String fileName, String typeStorage) {
        BucketFirebase bucketFirebase = this.defineKeyBucket(typeStorage);
        if (bucketFirebase == null) {
            return "Invalid storage type: " + typeStorage;
        } else {
            try {
                InputStream inputStream = ImageService.class.getClassLoader().getResourceAsStream(bucketFirebase.getKey());

                String var9;
                label54: {
                    String DOWNLOAD_URL;
                    try {
                        Credentials credentials = GoogleCredentials.fromStream(inputStream);
                        Storage storage = (Storage)((StorageOptions.Builder)StorageOptions.newBuilder().setCredentials(credentials)).build().getService();
                        Blob blob = storage.get(BlobId.of(bucketFirebase.getName(), fileName));
                        if (blob != null && blob.exists(new Blob.BlobSourceOption[0])) {
                            DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + bucketFirebase.getName() + "/o/%s?alt=media";
                            var9 = String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
                            break label54;
                        }

                        DOWNLOAD_URL = "File not found";
                    } catch (Throwable var11) {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Throwable var10) {
                                var11.addSuppressed(var10);
                            }
                        }

                        throw var11;
                    }

                    if (inputStream != null) {
                        inputStream.close();
                    }

                    return DOWNLOAD_URL;
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                return var9;
            } catch (IOException var12) {
                IOException e = var12;
                e.printStackTrace();
                return "Error while getting image link";
            }
        }
    }

    public BucketFirebase defineKeyBucket(String typeStorage) {
        switch (typeStorage.toLowerCase()) {
            case "barbearia" -> {
                return new BucketFirebase("naregua-upload.appspot.com", "naregua-upload-firebase-adminsdk-9hly7-454496d7de.json");
            }
            case "usuario" -> {
                return new BucketFirebase("upload-usuarios.appspot.com", "upload-usuarios-firebase-adminsdk-6r1vz-cfc61531f3.json");
            }
            case "comunidade" -> {
                return new BucketFirebase("upload-comunidade.appspot.com", "upload-comunidade-firebase-adminsdk-sxo3w-56c4ed2507.json");
            }
            case "galeria" -> {
                return new BucketFirebase("upload-galeria.appspot.com", "upload-galeria-firebase-adminsdk-d6ewd-34985e2190.json");
            }
            case "chat" -> {
                return new BucketFirebase("chat-5568f.appspot.com", "chat-5568f-firebase-adminsdk-unsxe-21134a8356.json");
            }
            default -> {
                return null;
            }
        }
    }
}