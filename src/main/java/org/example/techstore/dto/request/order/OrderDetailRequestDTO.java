package org.example.techstore.dto.request.order;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailRequestDTO {

    private Integer quantity;

    private BigDecimal unitPrice;

    private Long orderId;

    private Long productDetailId;
}
