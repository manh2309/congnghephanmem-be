package org.example.techstore.mapper;

import org.example.techstore.dto.request.product.ProductRequest;
import org.example.techstore.dto.request.specifications.SpecificationRequest;
import org.example.techstore.dto.response.ProductResponse;
import org.example.techstore.dto.response.SpecificationResponse;
import org.example.techstore.entity.Product;
import org.example.techstore.entity.Specification;
import org.example.techstore.repository.SpecificationRepository;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Specification toEntity(SpecificationRequest dto);

    SpecificationResponse toResponse(Specification entity);

    List<SpecificationResponse> toResponseList(List<Specification> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(SpecificationRequest dto, @MappingTarget Specification entity);
}
