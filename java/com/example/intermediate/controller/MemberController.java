package com.example.intermediate.controller;


import com.example.intermediate.domain.Member;
import com.example.intermediate.dto.request.LoginRequestDto;
import com.example.intermediate.dto.request.MemberRequestDto;
import com.example.intermediate.dto.request.TokenDto;
import com.example.intermediate.dto.response.ResponseDto;
import com.example.intermediate.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

  private final MemberService memberService;

  // 회원가입
  @PostMapping("/api/member/signup")
  public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
    return memberService.createMember(requestDto);
  }

  // 회원 가입 확인용
  @GetMapping("/api/member")
  public List<Member> showAllMember() {
    return memberService.showAllMember();
  }

  // 로그인
  @PostMapping("/api/member/login")
  public TokenDto login(@RequestBody @Valid LoginRequestDto requestDto,
                        HttpServletResponse response
  ) {
    return memberService.login(requestDto, response);
  }

//  @RequestMapping(value = "/api/auth/member/reissue", method = RequestMethod.POST)
//  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
//    return memberService.reissue(request, response);
//  }


  // 로그아웃
  @PostMapping("/api/auth/member/logout")
  public ResponseDto<?> logout(HttpServletRequest request) {
    return memberService.logout(request);
  }
}
