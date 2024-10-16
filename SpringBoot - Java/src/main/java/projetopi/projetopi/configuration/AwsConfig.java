package projetopi.projetopi.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value(value = "${AWS_ACCESS_KEY_ID}")
     String accessKey;

    @Value("${AWS_SECRET_ACCESS_KEY}")
    private String secretKey;

    @Value("${AWS_REGION}")
    private String region;


    public static void main(String[] args) {

        AwsConfig awsConfig = new AwsConfig();
        System.out.println(awsConfig.accessKey);

    }
    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region)
                .build();
    }
}