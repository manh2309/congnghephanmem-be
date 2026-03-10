package org.example.techstore.dto.response;

import lombok.*;
import org.example.techstore.dto.BaseResponse;
import org.example.techstore.entity.Configuration;
import org.example.techstore.entity.Product;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse extends BaseResponse {
    private Long id;
    private Configuration configuration;
    private Product product;
    private Integer quantity;
    private BigDecimal price;
}
