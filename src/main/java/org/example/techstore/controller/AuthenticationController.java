package org.example.techstore.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.auth.AuthRequest;
import org.example.techstore.dto.request.auth.RefreshTokenRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthRequest request) {
        ApiResponse response = authService.login(request);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@RequestBody RefreshTokenRequest request) {
        ApiResponse response = authService.refreshToken(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        ApiResponse response = authService.logout();
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
