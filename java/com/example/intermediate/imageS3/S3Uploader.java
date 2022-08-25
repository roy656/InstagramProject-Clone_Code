package com.example.intermediate.imageS3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;


//S3에 정적 파일을 올리는 역할을 하는 S3Uploader.java 파일입니다.

//코드를 보면 맨 처음에 AmazonS3Client 를 @RequiredArgsConstructor 어노테이션으로 의존성 주입을 받습니다.
// bucket 의 이름으로 @Value("${cloud.aws.s3.bucket}")을 사용합니다. 이는 S3Config 파일 에서 가져옵니다.

//각 코드 부분에 대한 이해는 MultipartFile 을 전달받아서 S3에 전달가능한 형태인 File 을 생성하고, 전환된 File 을 S3에 public 읽기 권한으로 전송합니다.
// 그리고 로컬에 생성된 File 을 삭제한 뒤, 업로드된 파일의 S3 URL 주소를 반환합니다.

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public S3Dto upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return uploadToS3(uploadFile, dirName);
    }

    private S3Dto uploadToS3(File uploadFile, String dirName) {
        String fileName = dirName + UUID.randomUUID() + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);
        S3Dto s3Dto = new S3Dto(fileName, uploadImageUrl);
        return s3Dto;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        System.out.println("convertFile = " + convertFile);
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}