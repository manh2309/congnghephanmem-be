package org.example.techstore.dto.response.account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatisticResponse {
    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String address;

    private Long totalOrders;
    private Long totalSpent;
}
