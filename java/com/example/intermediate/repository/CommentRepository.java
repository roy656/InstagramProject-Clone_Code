package com.example.intermediate.repository;


import com.example.intermediate.domain.Article;
import com.example.intermediate.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findAllByArticle(Article article);
}
