package org.ngcvfb.eventhubkz.Services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public S3Service(@Value("${aws.access-key-id}") String accessKey,
                     @Value("${aws.secret-access-key}") String secretKey,
                     @Value("${aws.s3.region}") String region,
                     @Value("${aws.s3.bucket-name}") String bucketName) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
        this.bucketName = bucketName;
    }

    public String uploadFile(String key, File file) {
        amazonS3.putObject(bucketName, key, file);
        return amazonS3.getUrl(bucketName, key).toString();
    }

    public void deleteFile(String key) {
        amazonS3.deleteObject(bucketName, key);
    }

}
