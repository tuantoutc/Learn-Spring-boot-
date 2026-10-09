package com.springmatter.relearnspringboot.common;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class FileStorageService {
    @Value("${minio.bucket}")
    private String bucketName;

    private final MinioClient minioClient;
    private final HybridEncryptionService encryptionService;

    public FileStorageService(HybridEncryptionService encryptionService) {
        this.encryptionService = encryptionService;
        this.minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("minioadmin", "miniopassword123")
                .build();
    }

    public void uploadEncryptedFile(String fileName, byte[] rawFileBytes) throws Exception {
//        byte[] fileBytes = file.getBytes();
//        EncryptedDataRes encrypted = encryptionService.encrypt(fileBytes);
//
//        // Ghép encryptedKey + iv + encryptedData (dùng dấu phân cách)
//        String combined = encrypted.encryptedKey() + "||"
//                         + encrypted.iv() + "||"
//                         + encrypted.encryptedData();

//        try (InputStream is = new ByteArrayInputStream(combined.getBytes())) {
//            minioClient.putObject(
//                PutObjectArgs.builder()
//                    .bucket(bucketName)
//                    .object(fileName)
//                    .stream(is, combined.length(), -1)
//                    .contentType(file.getContentType())
//                    .build()
//            );
//        }
//        return fileName;
        byte[] encryptedBinary = encryptionService.encryptToBinary(rawFileBytes);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(encryptedBinary)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(bais, encryptedBinary.length, -1)
                            .contentType("application/octet-stream")
                            .build());


        }
    }

    public byte[] downloadDecryptedFile(String fileName) throws Exception {
//        try (InputStream is = minioClient.getObject(
//                GetObjectArgs.builder()
//                    .bucket(bucketName)
//                    .object(fileName)
//                    .build())) {
//            byte[] content = is.readAllBytes();
//            String combined = new String(content, StandardCharsets.UTF_8);
//            String[] parts = combined.split("\\|\\|");
//            if (parts.length != 3) {
//                throw new RuntimeException("Invalid encrypted file format");
//            }
//            EncryptedDataRes encrypted = new EncryptedDataRes(parts[0], parts[1], parts[2]);
//            return encryptionService.decrypt(encrypted);
//        }
        try (InputStream is = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build())) {

            byte[] binaryContent = is.readAllBytes();
            return encryptionService.decryptFromBinary(binaryContent);
        }
    }
}