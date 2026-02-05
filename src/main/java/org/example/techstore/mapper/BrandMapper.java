package org.example.techstore.mapper;

import org.example.techstore.dto.request.brand.BrandRequest;
import org.example.techstore.dto.response.BrandResponse;
import org.example.techstore.entity.Brand;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Brand toEntity(BrandRequest dto);

    BrandResponse toResponse(Brand entity);

    List<BrandResponse> toResponseList(List<Brand> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(BrandRequest dto, @MappingTarget Brand entity);
}