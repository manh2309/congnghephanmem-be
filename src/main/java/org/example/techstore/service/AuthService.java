package org.example.techstore.service;

import org.example.techstore.dto.request.auth.AuthRequest;
import org.example.techstore.dto.request.auth.RefreshTokenRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.dto.response.auth.AuthResponse;

public interface AuthService {
    ApiResponse<AuthResponse> login(AuthRequest authRequest);
    ApiResponse<AuthResponse> refreshToken(RefreshTokenRequest request);
    ApiResponse<String> logout();
}