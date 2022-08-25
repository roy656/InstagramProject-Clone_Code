package com.example.intermediate.dto.response;


import com.example.intermediate.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {   // 게시글 생성 리스폰
  private Long id;
  private String nickname;
  private String content;
  private int heartCnt;
  private int commentCnt;
  private Boolean isLike;
  private String timeMsg;
  private List<Image> imgList;



  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ArticleDetailDto {    // 게시글 상세 조회 리스폰
    private Long id;
    private String nickname;
    private String content;
    private int heartCnt;
    private String timeMsg;
    private Boolean isLike;
    private List<Image> imgList;
    //  private List<Image> imgList;
    private List<CommentResponseDto> commentList;
  }


  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AllArticleResponseDto {   // 전체 게시글 조회 리스폰
    private Long id;
    private String nickname;
    private String content;
    private int heartCnt;
    private int commentCnt;
    private Boolean isLike;
    private List<Image> imgList;
    private String timeMsg;
    //  private List<Image> imgList;
  }
}
