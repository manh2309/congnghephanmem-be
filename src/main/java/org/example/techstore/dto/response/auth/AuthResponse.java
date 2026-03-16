package org.example.techstore.dto.response.auth;

import lombok.*;
import org.example.techstore.dto.response.account.UserStatisticResponse;
import org.example.techstore.entity.Account;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private Account user;
}