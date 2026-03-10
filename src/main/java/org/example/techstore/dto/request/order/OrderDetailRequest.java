package org.example.techstore.dto.request.order;

import lombok.Data;
import org.example.techstore.dto.request.product.ProductDetailRequest;

import java.math.BigDecimal;

@Data
public class OrderDetailRequest {
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDetailRequest productDetail;
}
