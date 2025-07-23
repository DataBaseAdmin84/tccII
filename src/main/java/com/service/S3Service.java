package com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

@Service
public class S3Service {
    private static final Logger log = LoggerFactory.getLogger(S3Service.class);

    // Campos agora são 'final' para garantir imutabilidade após a construção
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    // Injeção de dependência via construtor (melhor prática)
    public S3Service(S3Client s3Client, S3Presigner s3Presigner, @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
    }

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
                .contentType(multipartFile.getContentType()) // Boa prática: salvar o content type
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

        log.info("Arquivo {} enviado para o S3 com a chave: {}", originalFilename, key);
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

    public String gerarUrlPresignadaParaVisualizacao(String chaveArquivo, String nomeOriginal) {
        if (chaveArquivo == null || chaveArquivo.isBlank()) {
            log.warn("Tentativa de gerar URL para chave nula ou vazia.");
            return "#"; // Retorna um link morto seguro
        }
        try {
            // Garante que o nome do arquivo seja seguro para o cabeçalho HTTP
            String encodedFilename = URLEncoder.encode(nomeOriginal, StandardCharsets.UTF_8).replace("+", "%20");
            String contentDisposition = "inline; filename=\"" + encodedFilename + "\"";

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(chaveArquivo)
                    .responseContentDisposition(contentDisposition)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15)) // Tempo bom para vídeos e PDFs
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);
            String url = presignedGetObjectRequest.url().toString();
            log.info("URL pré-assinada (inline) gerada para a chave '{}'", chaveArquivo);
            return url;

        } catch (S3Exception e) {
            log.error("Erro do S3 ao gerar URL pré-assinada para a chave {}: {}", chaveArquivo, e.getMessage(), e);
            // Lançar uma exceção específica da aplicação seria ainda melhor, mas RuntimeException é aceitável.
            throw new RuntimeException("Não foi possível gerar a URL de visualização para o arquivo.", e);
        }
    }
}