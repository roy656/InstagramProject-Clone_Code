package com.example.intermediate.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_id")
    private Long id;

    private String nickname;

    @JoinColumn(name = "article_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;


    public Heart(Article article) {
        this.article = article;
    }

    public boolean isEmpty() {
        return true;
    }
}
