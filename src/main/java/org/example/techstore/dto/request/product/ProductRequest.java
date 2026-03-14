package org.example.techstore.dto.request.product;

import lombok.*;
import org.example.techstore.entity.Brand;
import org.example.techstore.entity.Category;
import org.example.techstore.entity.Image;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private Integer totalQuantity;
    private Long brandId;
    private Long categoryId;
    private String description;
    private List<ProductDetailCreateRequest> productDetails;
}
