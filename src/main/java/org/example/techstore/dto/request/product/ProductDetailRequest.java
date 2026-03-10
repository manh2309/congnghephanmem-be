package org.example.techstore.dto.request.product;

import lombok.*;
import org.example.techstore.entity.Configuration;
import org.example.techstore.entity.Product;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailRequest {
    private Configuration configuration;
    private Product product;
    private Integer quantity;
    private BigDecimal price;
}
