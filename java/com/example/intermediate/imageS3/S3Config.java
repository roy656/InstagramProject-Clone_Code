package com.example.intermediate.imageS3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// S3Config.java 파일을 생성하고, 전역적으로 사용할 변수들을 정의합니다.
// 그리고 AmazonS3Client 를 Bean 으로 등록합니다. 이는 아래 코드에서 의존성 주입을 할 때 사용됩니다.

@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;


    //AmazonS3Client 는 application.properties 에서 정의한 accessKey, secretKey, region 등을 이용하여 만든 클라이언트 객체입니다.
    // 이 객체를 통해 S3 버킷에 접근할 수 있습니다.

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
