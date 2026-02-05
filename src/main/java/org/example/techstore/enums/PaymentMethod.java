package org.example.techstore.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {

    COD(1, "Thanh toán khi nhận hàng"),
    ONLINE(2, "Thanh toán online");

    private final int code;
    private final String description;

    PaymentMethod(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PaymentMethod fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PaymentMethod method : values()) {
            if (method.code == code) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentMethod code: " + code);
    }

    public static String getDescription(Integer code) {
        PaymentMethod method = fromCode(code);
        return method != null ? method.description : "Không xác định";
    }
}