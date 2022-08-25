package com.example.intermediate.controller;


import com.example.intermediate.domain.UserDetailsImpl;
import com.example.intermediate.dto.response.ArticleResponseDto;
import com.example.intermediate.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class MypageController {

    private final MypageService mypageService;




    @GetMapping("/api/auth/mypage/myarticle")
    public List<ArticleResponseDto> mypageArticles(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        mypageService.mypageArticles(userDetails);
        return null;
    }
}
