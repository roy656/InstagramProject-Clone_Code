package com.example.intermediate.service;


import com.example.intermediate.domain.Member;
import com.example.intermediate.dto.request.LoginRequestDto;
import com.example.intermediate.dto.request.MemberRequestDto;
import com.example.intermediate.dto.request.TokenDto;
import com.example.intermediate.dto.response.MemberResponseDto;
import com.example.intermediate.dto.response.ResponseDto;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    //  private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;


    // 회원가입
    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getNickname())) {
            return ResponseDto.fail("DUPLICATED_NICKNAME",
                    "중복된 닉네임 입니다.");
        }


        // 프로젝트에선 비밀번호 일치 확인 안함
//    if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
//      return ResponseDto.fail("PASSWORDS_NOT_MATCHED",
//          "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
//    }

        Member member = Member.builder()
                .nickname(requestDto.getNickname())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .build()
        );
    }


    // 로그인
    @Transactional(readOnly = true)
    public TokenDto login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = memberRepository.findByNickname(requestDto.getNickname()).orElse(null);

        if (null == member) {
            throw new RuntimeException("사용자를 찾을 수 없습니다");
    }


//        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
//            return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");
//        }

//    UsernamePasswordAuthenticationToken authenticationToken =
//        new UsernamePasswordAuthenticationToken(requestDto.getNickname(), requestDto.getPassword());
//    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);     // 레포지토리에서 찾은 member 로 generateTokenDto 메소드를 실행해
                                                                        // 토큰을 발급해 TokenDto 에 담는다.
        tokenToHeaders(tokenDto, response);                             // 그리고 tokenToHeaders 메소드로 header 에 토큰을 전달.

        return tokenDto;                                                // 로그인이 완료되면 발급된 토큰을 담고있는 TokenDto 를 리턴.
    }

//  @Transactional
//  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
//    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token 이 유효하지 않습니다.");
//    }
//    Member member = tokenProvider.getMemberFromAuthentication();
//    if (null == member) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "사용자를 찾을 수 없습니다.");
//    }
//
//    Authentication authentication = tokenProvider.getAuthentication(request.getHeader("Access-Token"));
//    RefreshToken refreshToken = tokenProvider.isPresentRefreshToken(member);
//
//    if (!refreshToken.getValue().equals(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token 이 유효하지 않습니다.");
//    }
//
//    TokenDto tokenDto = tokenProvider.generateTokenDto(member);
//    refreshToken.updateValue(tokenDto.getRefreshToken());
//    tokenToHeaders(tokenDto, response);
//    return ResponseDto.success("success");
//  }

    // 회원 전체 목록 조회 (확인용)
    public List<Member> showAllMember() {
        List<Member> memberList = memberRepository.findAll();
        return memberList;
    }

    // 로그아웃
    public ResponseDto<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        return tokenProvider.deleteRefreshToken(member);
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

}
