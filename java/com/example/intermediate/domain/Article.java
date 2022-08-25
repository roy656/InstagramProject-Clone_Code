package com.example.intermediate.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "article_id")
  private Long id;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private String content;

  private int commentCnt;

  private int heartCnt;

  private Boolean isLike;

  @CreationTimestamp
  private Timestamp createdAt;    // ArticleService 에서 사용될때 타입 때문에 LocalDateTime 타입을 써야되는지 확인 해야함

  @Setter
  @Column
  private String timeMsg;

  @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Comment> commentList;

  @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Heart> heartList;

  @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Image> imgList;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;


  // 업데이트 메소드 프로젝트에서 노필요
//  public void update(ArticleRequestDto articleRequestDto) {
//    this.nickname = articleRequestDto.getNickname();
//    this.content = articleRequestDto.getContent();
//  }


  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }

  public void deletHeart(Heart heart) {
    heartList.remove(heart);
  }

  public void deletComment(Comment comment) {
    commentList.remove(comment);
  }

}
