package projetopi.projetopi.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.S3ResponseMetadata;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.analytics.AnalyticsConfiguration;
import com.amazonaws.services.s3.model.intelligenttiering.IntelligentTieringConfiguration;
import com.amazonaws.services.s3.model.inventory.InventoryConfiguration;
import com.amazonaws.services.s3.model.metrics.MetricsConfiguration;
import com.amazonaws.services.s3.model.ownership.OwnershipControls;
import com.amazonaws.services.s3.waiters.AmazonS3Waiters;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Service("s3StorageService")
public class StorageService {

    @Autowired
    private AmazonS3 amazonS3;

    private final String bucketName = "nareguabucket";

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        amazonS3.putObject(bucketName, fileName, file.getInputStream(), null);
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public byte[] getBlob(String blobName) throws IOException {
        S3Object s3Object = amazonS3.getObject(bucketName, blobName);
        try (S3ObjectInputStream inputStream = s3Object.getObjectContent();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            return outputStream.toByteArray();
        }
    }

    public List<byte[]> getBlobArray(List<String> blobNames) throws IOException {
        List<byte[]> imageBytesList = new ArrayList<>();
        for (String blobName : blobNames) {
            imageBytesList.add(getBlob(blobName));
        }
        return imageBytesList;
    }

    public String getBlobUrl(String blobName) {
        if (blobName == null) return null;
        return amazonS3.getUrl(bucketName, blobName).toString();
    }
}
