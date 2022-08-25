package com.example.intermediate.controller;


import com.example.intermediate.domain.UserDetailsImpl;
import com.example.intermediate.dto.request.CommentRequestDto;
import com.example.intermediate.dto.response.ResponseDto;
import com.example.intermediate.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
public class CommentController {

  private final CommentService commentService;

  // 댓글 생성
  @RequestMapping(value = "/api/auth/comment/{articleid}", method = RequestMethod.POST)
  public ResponseDto<?> createComment(@PathVariable Long articleid, @RequestBody CommentRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.createComment(articleid, requestDto, userDetails);
  }

  // 댓글 전체 조회 (프로젝트에선 노필요)
//  @RequestMapping(value = "/api/comment/{id}", method = RequestMethod.GET)
//  public ResponseDto<?> getAllComments(@PathVariable Long id) {
//    return commentService.getAllCommentsByArticle(id);
//  }

  // 댓글 삭제
  @RequestMapping(value = "/api/auth/comment/{articleId}/{commentId}", method = RequestMethod.DELETE)
  public ResponseDto<?> deleteComment(@PathVariable Long articleId, @PathVariable Long commentId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.deleteComment(articleId, commentId, userDetails);
  }

  // 프로젝트에선 댓글 수정 노필요
//  @RequestMapping(value = "/api/auth/comment/{id}", method = RequestMethod.PUT)
//  public ResponseDto<?> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
//      HttpServletRequest request) {
//    return commentService.updateComment(id, requestDto, request);
//  }
}
