package org.example.techstore.mapper;

import org.example.techstore.dto.request.product.ProductDetailRequest;
import org.example.techstore.dto.request.product.ProductRequest;
import org.example.techstore.dto.response.ProductDetailResponse;
import org.example.techstore.dto.response.ProductResponse;
import org.example.techstore.entity.Product;
import org.example.techstore.entity.ProductDetail;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductDetailMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    ProductDetail toEntity(ProductDetailRequest dto);

    ProductDetailResponse toResponse(ProductDetail entity);

    List<ProductDetailResponse> toResponseList(List<ProductDetail> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProductDetailRequest dto, @MappingTarget ProductDetail entity);
}
