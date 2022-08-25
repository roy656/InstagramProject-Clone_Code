package com.example.intermediate.service;


import com.example.intermediate.domain.*;
import com.example.intermediate.dto.request.ArticleRequestDto;
import com.example.intermediate.dto.response.ArticleResponseDto;
import com.example.intermediate.dto.response.CommentResponseDto;
import com.example.intermediate.dto.response.ResponseDto;
import com.example.intermediate.imageS3.S3Dto;
import com.example.intermediate.imageS3.S3Uploader;
import com.example.intermediate.repository.ArticleRepository;
import com.example.intermediate.repository.HeartRepository;
import com.example.intermediate.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final HeartRepository heartRepository;
    private final S3Uploader s3Uploader;

    private final ImageRepository imageRepository;


    // 게시글 생성
    @Transactional
    public ResponseDto<?> createArticle(ArticleRequestDto requestDto,
                                        UserDetailsImpl userDetails,
                                        List<MultipartFile> multipartFiles) throws IOException {

        if (multipartFiles.isEmpty())
            throw new NullPointerException("이미지 파일이 필요합니다.");


        Member member = userDetails.getMember();

        // *빌더 패턴의 사용*
        Article article = Article.builder()
                .nickname(requestDto.getNickname())
                .content(requestDto.getContent())
                .member(member)
                .build();
        articleRepository.save(article);
                                                                // 이미지 업로드 Service 로직.
        List<Image> imgList = new ArrayList<>();                // 이미지 객체를 담을 새 리스트 생성

        for (MultipartFile file : multipartFiles) {             // 클라이언트로 부터 받은 MultipartFile 들을 하나씩 뽑아내

            S3Dto convertedFileDto = s3Uploader.upload(file, "static/");    // s3Uploader 의 upload 메소드로
                                                                                    // MultipartFile -> File 로 변환 후 리턴
            Image image = Image.builder()                       // builder 패턴으로 Image 객체 생성
                    .urlPath(convertedFileDto.getUrlPath())
                    .imgUrl(convertedFileDto.getImgUrl())
                    .article(article)
                    .build();

            imgList.add(image);                                 // 위에서 생선해놓은 리스트에 담는다
            imageRepository.save(image);                        // 레퍼지토리에 저장.
        }


        return ResponseDto.success(ArticleResponseDto.builder()
                .id(article.getId())
                .nickname(requestDto.getNickname())
                .content(requestDto.getContent())
                .commentCnt(0)
                .heartCnt(0)
                .timeMsg("방금전")
                .isLike(false)
                .imgList(imgList)
                .build());

    }


    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getArticle(Long id, UserDetailsImpl userDetails) {        // userDetails 는 isLike 에 사용 예정.

        Article article = isPresentArticle(id);
        if (null == article) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        article.setTimeMsg(Time.calculateTime(article.getCreatedAt()));   // calculateTime 의 파라미터가 Date 타입

        List<Comment> commentList = article.getCommentList();
        List<CommentResponseDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .timeMsg(comment.getTimeMsg())
                            .build()
            );
        }

        return ResponseDto.success(             // API 명세서 대로 리스폰
                ArticleResponseDto.ArticleDetailDto.builder()
                        .id(article.getId())
                        .nickname(article.getNickname())
                        .content(article.getContent())
                        .heartCnt(article.getHeartList().size())
                        .timeMsg(article.getTimeMsg())
                        .isLike(isPresentHeart(article, article.getNickname())) // isPresentHeart 의 파라미터에 article 과 nickname 이 필요한데
                        .imgList(article.getImgList())                          // userDetails.GetMember.Get.Nickname 으로 가져오는게 맞으나
                        .commentList(commentDtoList)                            // 에러가 남. 그래서 임시방편으로 일단 article 에서 가져오는걸로 해놓음.
                        .build()                                                // 그러면 지금은 로그인유저 입장에서 좋아요를 눌렀는지가 아니게됨.
        );
    }


    // 게시글 전체 조회
    @Transactional(readOnly = true)     // Dto 안에 이너 클래스 사용; 빌더패턴으로 코드 더러워짐; 생성자가 나을듯
    public List<ArticleResponseDto.AllArticleResponseDto> getAllArticle(UserDetailsImpl userDetails) {
        List<Article> articleList = articleRepository.findAll();
        List<ArticleResponseDto.AllArticleResponseDto> responseDtoList = new ArrayList<>();

        for (Article article : articleList) {
            article.setTimeMsg(Time.calculateTime(article.getCreatedAt()));

            responseDtoList.add(ArticleResponseDto.AllArticleResponseDto.builder()
                    .id(article.getId())
                    .nickname(article.getNickname())
                    .content(article.getContent())
                    .commentCnt(article.getCommentList().size())
                    .heartCnt(article.getHeartList().size())
                    .isLike(isPresentHeart(article, article.getNickname())) // isPresentHeart 의 파라미터에 article 과 nickname 이 필요한데
                    .imgList(article.getImgList())                          // userDetails.GetMember.Get.Nickname 으로 가져오는게 맞으나
                    .timeMsg(article.getTimeMsg())                          // 에러가 남. 그래서 임시방편으로 일단 article 에서 가져오는걸로 해놓음.
                    .build());                                              // 그러면 지금은 로그인유저 입장에서 좋아요를 눌렀는지가 아니게됨.
        }
        return responseDtoList;
    }


    // 게시글 수정 (프로젝트에선 필요없음)
//  @Transactional
//  public ResponseDto<Article> updateArticle(Long id, ArticleRequestDto requestDto, HttpServletRequest request) {
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
//    Article article = isPresentArticle(id);
//    if (null == article) {
//      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
//    }
//
//    if (article.validateMember(member)) {
//      return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
//    }
//
//    article.update(requestDto);
//    return ResponseDto.success(article);
//  }


    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deleteArticle(Long id, UserDetailsImpl userDetails) {


        Member member = userDetails.getMember();

        Article article = isPresentArticle(id);
        if (null == article) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (article.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        articleRepository.delete(article);
        return ResponseDto.success("delete success");
    }

    @Transactional(readOnly = true)
    public Article isPresentArticle(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        return optionalArticle.orElse(null);
    }

    public Boolean isPresentHeart(Article article, String nickname) {
        return heartRepository.existsHeartByNicknameAndArticle(nickname, article);
    }

//    @Transactional
//    public Member validateMember(HttpServletRequest request) {
//        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//            return null;
//        }
//        return tokenProvider.getMemberFromAuthentication();
//    }

}
