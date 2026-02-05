package org.example.techstore.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING(1, "Chờ xử lý"),
    CONFIRMED(2, "Đã xác nhận"),
    SHIPPING(3, "Đang giao hàng"),
    DELIVERED(4, "Đã giao"),
    CANCELED(5, "Đã huỷ");

    private final int code;
    private final String description;

    OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static OrderStatus fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus code: " + code);
    }

    public static String getDescription(Integer code) {
        OrderStatus status = fromCode(code);
        return status != null ? status.description : "Không xác định";
    }
}