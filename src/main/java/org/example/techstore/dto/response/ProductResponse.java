package org.example.techstore.dto.response;

import lombok.*;
import org.example.techstore.dto.BaseResponse;
import org.example.techstore.entity.Brand;
import org.example.techstore.entity.Category;
import org.example.techstore.entity.Image;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends BaseResponse {
    private Long id;
    private String productCode;
    private String name;
    private Integer totalQuantity;
    private Brand brand;
    private Category category;
    private String description;
    private List<Image> images;
}
