package com._pi.benepick.global.objectStorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;


@Configuration
public class ObjectStorageConfig {
    @Value("${cloud.kakao.object-storage.endpoint}")
    private String endpoint;
    @Value("${cloud.kakao.object-storage.access-key}")
    private String accessKey;
    @Value("${cloud.kakao.object-storage.secret-key}")
    private String secretKey;
    @Value("${cloud.kakao.object-storage.region}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)))
            .endpointOverride(URI.create(endpoint))
            .forcePathStyle(true)
            .build();
    }
}
