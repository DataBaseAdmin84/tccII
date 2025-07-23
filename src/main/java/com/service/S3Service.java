package com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
public class S3Service {
    private static final Logger log = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadArquivo(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String key = UUID.randomUUID().toString() + fileExtension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

        log.info("Arquivo {} enviado para o S3 com a chave: {}", originalFilename, key);

        // MUDANÇA IMPORTANTE: Retorna APENAS a chave.
        return key;
    }

    public void excluirArquivo(String chaveArquivo) {
        if (chaveArquivo == null || chaveArquivo.isBlank()) {
            log.warn("Tentativa de exclusão de arquivo com chave nula ou vazia.");
            return;
        }

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(chaveArquivo)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("Arquivo com a chave {} foi excluído com sucesso do bucket {}.", chaveArquivo, bucketName);

        } catch (S3Exception e) {
            log.error("Erro ao excluir o arquivo com chave {} do S3: {}", chaveArquivo, e.awsErrorDetails().errorMessage(), e);
        }
    }

    public String gerarUrlPresignada(String chaveArquivo) {
        if (chaveArquivo == null || chaveArquivo.isBlank()) {
            log.warn("Tentativa de gerar URL para chave nula ou vazia.");
            return "#"; // Retorna um link inofensivo para evitar erros no frontend
        }
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(chaveArquivo)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10)) // O link expira em 10 minutos
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);
            String url = presignedGetObjectRequest.url().toString();
            log.info("URL pré-assinada gerada para a chave '{}'", chaveArquivo);
            return url;

        } catch (S3Exception e) {
            log.error("Erro do S3 ao gerar URL pré-assinada para a chave {}: {}", chaveArquivo, e.getMessage(), e);
            throw new RuntimeException("Não foi possível gerar a URL de download para o arquivo.", e);
        }
    }
}