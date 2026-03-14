package org.example.techstore.dto.request.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailCreateRequest {
    private Long configurationId;
    private Integer quantity;
    private BigDecimal price;
}
