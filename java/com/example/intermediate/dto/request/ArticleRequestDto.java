package com.example.intermediate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequestDto {
  private String nickname;
  private String content;
}
