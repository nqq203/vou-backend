package com.vou.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

@Service
public class StorageService {
    @Autowired
    private S3Client s3Client;

    public String uploadImage(MultipartFile avatarFile) throws IOException {
        String bucketName = "vou-storage";

        try {
            String keyName = "avatar/" + System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
            String contentType = avatarFile.getContentType();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType(contentType)
                    .acl("public-read")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(avatarFile.getInputStream(), avatarFile.getSize()));
            return "https://" + bucketName + ".s3.amazonaws.com/" + keyName;
        } catch (S3Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
