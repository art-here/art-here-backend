package com.backend.arthere.auth.presentation;

import com.backend.arthere.auth.application.FakeLoginService;
import com.backend.arthere.auth.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/oauth2")
@RestController
public class FakeLoginController {

    private final FakeLoginService fakeLoginService;

    @PostMapping("/authorization")
    public ResponseEntity<TokenResponse> login() {
        return ResponseEntity.ok().body(fakeLoginService.login());
    }
}
