package org.example.techstore.dto.request.account;

import lombok.Data;

@Data
public class CreateAccountRequest {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private Long roleId;
}