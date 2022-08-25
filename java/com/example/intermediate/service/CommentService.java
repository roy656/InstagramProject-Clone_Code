package com.example.intermediate.service;


import com.example.intermediate.domain.*;
import com.example.intermediate.dto.request.CommentRequestDto;
import com.example.intermediate.dto.response.CommentResponseDto;
import com.example.intermediate.dto.response.ResponseDto;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final TokenProvider tokenProvider;
    private final ArticleService articleService;


    // 댓글 생성
    @Transactional
    public ResponseDto<?> createComment(Long articleid, CommentRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Article article = articleService.isPresentArticle(articleid);
        if (null == article) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        Comment comment = Comment.builder()
                .member(member)
                .article(article)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);

        comment.setTimeMsg(Time.calculateTime(comment.getCreatedAt()));     // 댓글 생성 시점
        article.getCommentList().add(comment);

        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .author(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .timeMsg(comment.getTimeMsg())
                        .build()
        );
    }

    // 댓글 삭제
    @Transactional
    public ResponseDto<?> deleteComment(Long articleId, Long commentId, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Article article = articleService.isPresentArticle(articleId);

        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        article.deletComment(comment);

        commentRepository.delete(comment);
        return ResponseDto.success("true");
    }


    // 편의 메소드
    @Transactional(readOnly = true)
    public Comment isPresentComment(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.orElse(null);
    }


    // 해당 게시물 댓글 전체 조회 (프로젝트에선 노필요)
//    @Transactional(readOnly = true)
//    public ResponseDto<?> getAllCommentsByArticle(Long articleId) {
//        Article article = articleService.isPresentArticle(articleId);
//        if (null == article) {
//            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
//        }
//
//        List<Comment> commentList = commentRepository.findAllByArticle(article);
//        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
//
//        for (Comment comment : commentList) {
//            commentResponseDtoList.add(
//                    CommentResponseDto.builder()
//                            .id(comment.getId())
//                            .author(comment.getMember().getNickname())
//                            .content(comment.getContent())
//                            .timeMsg(comment.getTimeMsg())
//                            .build()
//            );
//        }
//        return ResponseDto.success(commentResponseDtoList);
//    }


    // 프로젝트에선 댓글 수정 노필요
//  @Transactional
//  public ResponseDto<?> updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
//    if (null == request.getHeader("Refresh-Token")) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "로그인이 필요합니다.");
//    }
//
//    if (null == request.getHeader("Authorization")) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "로그인이 필요합니다.");
//    }
//
//    Member member = validateMember(request);
//    if (null == member) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//    }
//
//    Article article = articleService.isPresentArticle(requestDto.getArticleId());
//    if (null == article) {
//      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
//    }
//
//    Comment comment = isPresentComment(id);
//    if (null == comment) {
//      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
//    }
//
//    if (comment.validateMember(member)) {
//      return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
//    }
//
//    comment.update(requestDto);
//    return ResponseDto.success(
//        CommentResponseDto.builder()
//            .id(comment.getId())
//            .author(comment.getMember().getNickname())
//            .content(comment.getContent())
//            .createdAt(comment.getCreatedAt())
//            .build()
//    );
//  }




//    @Transactional
//    public Member validateMember(HttpServletRequest request) {
//        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//            return null;
//        }
//        return tokenProvider.getMemberFromAuthentication();
//    }
}
