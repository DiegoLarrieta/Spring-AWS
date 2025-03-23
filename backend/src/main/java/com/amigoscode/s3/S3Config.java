package com.amigoscode.s3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {
        // Región definida directamente aquí
        Region region = Region.US_EAST_2; // Cambia si usas otra
        System.out.println("Configurando S3Client con región: " + region);
        S3Client client = S3Client.builder()
                .region(region)
                .build();
        return client;
        

    }
}
