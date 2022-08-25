package com.example.intermediate.imageS3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class S3Dto {
    String urlPath;     // 파일이름
    String imgUrl;
}