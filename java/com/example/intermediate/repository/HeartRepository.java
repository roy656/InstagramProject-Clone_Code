package com.example.intermediate.repository;


import com.example.intermediate.domain.Article;
import com.example.intermediate.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByArticleId(Long articleid);
    Optional<Heart> findByNicknameAndArticle(String nickname, Article article);

    Boolean existsByNicknameAndArticle(String nickname, Article article);
    Boolean existsHeartByNicknameAndArticle(String nickname, Article article);


}
