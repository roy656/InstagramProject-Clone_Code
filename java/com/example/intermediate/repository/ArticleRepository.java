package com.example.intermediate.repository;


import com.example.intermediate.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findAllByNickname(String nickname);
}
