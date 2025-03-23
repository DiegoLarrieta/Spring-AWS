package com.amigoscode.s3;

import java.io.IOException;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final S3Client s3;

    // Constructor should be public for Spring to inject the S3Client bean
    public S3Service(S3Client s3) {
        System.out.println("➡️  Entrando a S3Config");
        this.s3 = s3;
    }

    // PUT
    public void putObject(String bucketName, String key, byte[] file) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3.putObject(objectRequest, RequestBody.fromBytes(file)); // Correct RequestBody usage
    }

    // GET
    public byte[] getObject(String bucketName, String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> res = s3.getObject(getObjectRequest);

        try {
            return res.readAllBytes(); // Reads all bytes from the stream
        } catch (IOException e) {
            throw new RuntimeException("Error reading file from S3", e);
        }
    }
}