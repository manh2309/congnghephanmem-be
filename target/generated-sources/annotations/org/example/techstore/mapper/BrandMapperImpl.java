package org.example.techstore.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.techstore.dto.request.brand.BrandRequest;
import org.example.techstore.dto.response.BrandResponse;
import org.example.techstore.entity.Brand;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-10T11:43:10+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class BrandMapperImpl implements BrandMapper {

    @Override
    public Brand toEntity(BrandRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Brand.BrandBuilder<?, ?> brand = Brand.builder();

        brand.brandCode( dto.getBrandCode() );
        brand.name( dto.getName() );

        return brand.build();
    }

    @Override
    public BrandResponse toResponse(Brand entity) {
        if ( entity == null ) {
            return null;
        }

        BrandResponse.BrandResponseBuilder brandResponse = BrandResponse.builder();

        brandResponse.id( entity.getId() );
        brandResponse.brandCode( entity.getBrandCode() );
        brandResponse.name( entity.getName() );

        return brandResponse.build();
    }

    @Override
    public List<BrandResponse> toResponseList(List<Brand> entities) {
        if ( entities == null ) {
            return null;
        }

        List<BrandResponse> list = new ArrayList<BrandResponse>( entities.size() );
        for ( Brand brand : entities ) {
            list.add( toResponse( brand ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(BrandRequest dto, Brand entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getBrandCode() != null ) {
            entity.setBrandCode( dto.getBrandCode() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
    }
}
