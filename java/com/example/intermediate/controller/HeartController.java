package com.example.intermediate.controller;



import com.example.intermediate.domain.UserDetailsImpl;
import com.example.intermediate.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HeartController {

    private final HeartService heartService;

//
//    // 좋아요 생성
//    @PostMapping("/api/auth/heart/{articleId}")
//    public boolean addHeart(@PathVariable Long articleId, HttpServletRequest request) {
//        heartService.addHeart(articleId, request);
//        return true;
//    }

    @PostMapping("/api/auth/heart/{articleId}")
    public boolean addHeart(@PathVariable Long articleId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        heartService.addHeart(articleId, userDetails);
        return true;
    }
}
