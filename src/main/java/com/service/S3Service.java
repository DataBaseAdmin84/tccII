package com.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "seu-bucket-s3"; // Substitua pelo seu bucket

    public S3Service() {
        this.s3Client = S3Client.builder()
                .region(software.amazon.awssdk.regions.Region.US_EAST_1) // Altere para sua região
                .build();
    }

    public String uploadFile(MultipartFile file, String filePath) throws IOException {
        String fileName = file.getOriginalFilename();
        // Envia o arquivo para o S3
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        // Retorna o caminho S3 + caminho local (opcional)
        return "s3://" + bucketName + "/" + fileName + " | Caminho local: " + filePath;
    }
}
