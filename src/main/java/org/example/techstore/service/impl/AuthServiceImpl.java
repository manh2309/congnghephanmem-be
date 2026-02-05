package org.example.techstore.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.config.security.JwtUtil;
import org.example.techstore.dto.request.auth.AuthRequest;
import org.example.techstore.dto.request.auth.RefreshTokenRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.dto.response.auth.AuthResponse;
import org.example.techstore.entity.Account;
import org.example.techstore.exception.AppException;
import org.example.techstore.exception.StatusCode;
import org.example.techstore.repository.AccountRepository;
import org.example.techstore.service.AuthService;
import org.example.techstore.utils.Constant;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    AuthenticationManager authenticationManager;
    JwtUtil jwtUtil;
    AccountRepository accountRepository;
    @Override
    public ApiResponse<AuthResponse> login(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            Account accountEntity = accountRepository.findByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.ACCOUNT))));

            String token = jwtUtil.generateToken(accountEntity);
            String refreshToken = jwtUtil.generateRefreshToken(accountEntity);

            AuthResponse authResponse = AuthResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();

            return ApiResponse.<AuthResponse>builder()
                    .code(200)
                    .message(Constant.MESSAGE.LOGIN_SUCCESS)
                    .result(authResponse)
                    .build();

        } catch (BadCredentialsException e) {
            return ApiResponse.<AuthResponse>builder()
                    .code(401)
                    .message(Constant.ERROR_MESSAGE.USER_PASS_FOUND)
                    .result(null)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.<AuthResponse>builder()
                    .code(500)
                    .message("Lỗi hệ thống: " + e.getMessage())
                    .result(null)
                    .build();
        }
    }

    @Override
    public ApiResponse<AuthResponse> refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ApiResponse.<AuthResponse>builder()
                    .code(400)
                    .message(Constant.ERROR_MESSAGE.TOKEN_REQUIRE_FOUND)
                    .result(null)
                    .build();
        }

        String username;
        try {
            username = jwtUtil.extractUsername(refreshToken);
        } catch (Exception e) {
            return ApiResponse.<AuthResponse>builder().code(403).message(Constant.ERROR_MESSAGE.TOKEN_FOUND).build();
        }

        Account accountEntity = accountRepository.findByUsername(username).orElse(null);

        if (accountEntity != null && jwtUtil.isRefreshTokenValid(refreshToken, username)) {
            String newAccessToken = jwtUtil.generateToken(accountEntity);
            String newRefreshToken = jwtUtil.generateRefreshToken(accountEntity);

            AuthResponse response = AuthResponse.builder()
                    .token(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();

            return ApiResponse.<AuthResponse>builder().code(200).message(Constant.MESSAGE.REFRESH_TOKEN_SUCCESS).result(response).build();
        }

        return ApiResponse.<AuthResponse>builder().code(403).message(Constant.ERROR_MESSAGE.TOKEN_NOT_VALID).build();
    }

    @Override
    public ApiResponse<String> logout() {
        return ApiResponse.<String>builder()
                .code(200)
                .message(Constant.MESSAGE.LOGOUT_SUCCESS)
                .result(null)
                .build();
    }
}
