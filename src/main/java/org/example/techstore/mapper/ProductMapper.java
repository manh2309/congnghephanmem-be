package org.example.techstore.mapper;

import org.example.techstore.dto.request.product.ProductRequest;
import org.example.techstore.dto.response.ProductResponse;
import org.example.techstore.entity.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "productCode", ignore = true)
    Product toEntity(ProductRequest dto);

    ProductResponse toResponse(Product entity);

    List<ProductResponse> toResponseList(List<Product> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProductRequest dto, @MappingTarget Product entity);
}
