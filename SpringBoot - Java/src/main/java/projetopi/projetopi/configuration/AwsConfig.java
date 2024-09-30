package projetopi.projetopi.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    // Coloque suas credenciais da AWS diretamente aqui
    private String accessKey = "ASIAVUGWMWWEHQOLQEHP"; // Substitua por sua Access Key
    private String secretKey = "y+qKmixqUky4SAQWjCkYfl4qsgnA8UvqbuzPfPTB"; // Substitua por sua Secret Key
    private String region = "us-east-1"; // Substitua pela sua região, ex: us-east-1

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region)
                .build();
    }
}
