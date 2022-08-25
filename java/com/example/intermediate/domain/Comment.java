package com.example.intermediate.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @JoinColumn(name = "article_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  private Article article;

  @Column(nullable = false)
  private String content;

  @CreationTimestamp
  private Timestamp createdAt;

  @Setter
  @Column
  private String timeMsg;

//  public void update(CommentRequestDto commentRequestDto) {
//    this.content = commentRequestDto.getContent();
//  }



  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }
}
