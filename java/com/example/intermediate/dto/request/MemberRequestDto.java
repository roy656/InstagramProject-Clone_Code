package com.example.intermediate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {


  private String email;

//  @NotBlank
//  @Size(min = 4, max = 12)
//  @Pattern(regexp = "[a-zA-Z\\d]*${3,12}")
  private String nickname;

//  @NotBlank
//  @Size(min = 4, max = 32)
//  @Pattern(regexp = "[a-z\\d]*${3,32}")
  private String password;

//  @NotBlank
  private String passwordConfirm;
}
