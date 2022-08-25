package com.example.intermediate.service;



import com.example.intermediate.domain.Article;
import com.example.intermediate.domain.Heart;
import com.example.intermediate.domain.UserDetailsImpl;
import com.example.intermediate.repository.ArticleRepository;
import com.example.intermediate.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class HeartService {

    private final HeartRepository heartRepository;
    private final ArticleRepository articleRepository;


    // 좋아요 생성
    @Transactional
    public void addHeart(Long articleId, UserDetailsImpl userDetails) {


        Article foundArticle = articleRepository.findById(articleId).orElseThrow(       // 게시글 repo에서 해당 게시글을 찾고
                () -> new NullPointerException("해당 게시글을 찾을수 없습니다."));

        String nickname = userDetails.getMember().getNickname();    // userDetails 를 이용하면 간단히 멤버나 닉네임을 가져올수 있음.

        Heart heart = alreadyHeart(nickname, foundArticle);

        if (heart == null) {                    // 위의 검사 메소드가 true 이면 좋아요 생성,
            heartRepository.save(Heart.builder()
                    .nickname(nickname)
                    .article(foundArticle)
                    .build());

            foundArticle.getHeartList().add(heart);


        } else {                                             // false 면 이미 있는 좋아요 삭제 (한번 더 눌렀을경우 좋아요 취소 기능)
            heartRepository.delete(heart);
            foundArticle.deletHeart(heart);                  // 엔티티의 heartList 에서 도 제거.
        }
    }


    // 해당 게시물에 좋아요가 안눌러져있는지 검사
    public Heart alreadyHeart(String nickname, Article article) {
        return heartRepository.findByNicknameAndArticle(nickname, article).orElse(null);
    }
}
