package com.example.intermediate.service;



import com.example.intermediate.domain.Article;
import com.example.intermediate.domain.Time;
import com.example.intermediate.domain.UserDetailsImpl;
import com.example.intermediate.dto.response.ArticleResponseDto;
import com.example.intermediate.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MypageService {

    private final ArticleRepository articleRepository;


    public List<ArticleResponseDto> mypageArticles(UserDetailsImpl userDetails) {

        String nickname = userDetails.getMember().getNickname();
        List<Article> articleList = articleRepository.findAllByNickname(nickname);
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();

        for (Article article : articleList) {
            articleResponseDtoList.add(ArticleResponseDto.builder()
                    .id(article.getId())
                    .nickname(nickname)
                    .content(article.getContent())
                    .heartCnt(article.getHeartList().size())
                    .timeMsg(Time.calculateTime(article.getCreatedAt()))
                    .commentCnt(article.getCommentList().size())
                    .build());
        }

        return articleResponseDtoList;
    }
}
