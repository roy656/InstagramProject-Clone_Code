package com.example.intermediate.controller;


import com.example.intermediate.domain.UserDetailsImpl;
import com.example.intermediate.dto.request.ArticleRequestDto;
import com.example.intermediate.dto.response.ArticleResponseDto;
import com.example.intermediate.dto.response.ResponseDto;
import com.example.intermediate.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ArticleController {

  private final ArticleService articleService;


  // 게시글 생성
  @PostMapping(value = "/api/auth/article", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseDto<?> createArticle(@RequestPart(value = "dto") ArticleRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestPart(required = false, value="files") List<MultipartFile> multipartFiles) throws IOException {
    return articleService.createArticle(requestDto, userDetails, multipartFiles);
  }

  // 게시글 상세 조회
  @GetMapping("/api/article/{id}")
  public ResponseDto<?> getArticle(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return articleService.getArticle(id, userDetails);
  }


  // 게시글 전체 조회
  @GetMapping("/api/article")
  public List<ArticleResponseDto.AllArticleResponseDto> getAllArticle(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return articleService.getAllArticle(userDetails);
  }

  // 게시글 수정 (프로젝트에선 필요 없음)
//  @PutMapping("/api/auth/article/{id}")
//  public ResponseDto<?> updateArticle(@PathVariable Long id, @RequestBody ArticleRequestDto articleRequestDto,
//      HttpServletRequest request) {
//    return articleService.updateArticle(id, articleRequestDto, request);
//  }

  // 게시글 삭제
  @DeleteMapping("/api/auth/article/{id}")
  public ResponseDto<?> deleteArticle(@PathVariable Long id,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return articleService.deleteArticle(id, userDetails);
  }

}
