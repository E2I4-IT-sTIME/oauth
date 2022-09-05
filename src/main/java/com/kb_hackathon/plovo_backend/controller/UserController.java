package com.kb_hackathon.plovo_backend.controller;

import com.kb_hackathon.plovo_backend.config.jwt.JwtProperties;
import com.kb_hackathon.plovo_backend.model.User;
import com.kb_hackathon.plovo_backend.model.kakaoLoginDto.OauthToken;
import com.kb_hackathon.plovo_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public String login(){
        return "home";
    }

    @GetMapping("/home")
    public String home(){
        return "/home.html";
    }

    @GetMapping("/oauth/token")
    public ResponseEntity getLogin(@RequestParam("code") String code) {

        // 넘어온 인가 코드를 통해 access_token 발급
        OauthToken oauthToken = userService.getAccessToken(code);

        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장
        String jwtToken = userService.SaveUserAndGetToken(oauthToken.getAccess_token());

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("success");
    }

    // jwt 토큰으로 유저정보 요청하기
    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request) {

        User user = userService.getUser(request);

        return ResponseEntity.ok().body(user);
    }
}
