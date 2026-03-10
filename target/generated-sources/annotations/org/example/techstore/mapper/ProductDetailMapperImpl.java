package org.example.techstore.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.techstore.dto.request.product.ProductDetailRequest;
import org.example.techstore.dto.response.ProductDetailResponse;
import org.example.techstore.entity.Configuration;
import org.example.techstore.entity.Product;
import org.example.techstore.entity.ProductDetail;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-10T11:43:10+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ProductDetailMapperImpl implements ProductDetailMapper {

    @Override
    public ProductDetail toEntity(ProductDetailRequest dto) {
        if ( dto == null ) {
            return null;
        }

        ProductDetail.ProductDetailBuilder<?, ?> productDetail = ProductDetail.builder();

        productDetail.quantity( dto.getQuantity() );
        productDetail.price( dto.getPrice() );

        return productDetail.build();
    }

    @Override
    public ProductDetailResponse toResponse(ProductDetail entity) {
        if ( entity == null ) {
            return null;
        }

        ProductDetailResponse.ProductDetailResponseBuilder productDetailResponse = ProductDetailResponse.builder();

        productDetailResponse.productName( entityProductName( entity ) );
        productDetailResponse.configurationName( entityConfigurationName( entity ) );
        productDetailResponse.id( entity.getId() );
        productDetailResponse.quantity( entity.getQuantity() );
        productDetailResponse.price( entity.getPrice() );

        return productDetailResponse.build();
    }

    @Override
    public List<ProductDetailResponse> toResponseList(List<ProductDetail> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProductDetailResponse> list = new ArrayList<ProductDetailResponse>( entities.size() );
        for ( ProductDetail productDetail : entities ) {
            list.add( toResponse( productDetail ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(ProductDetailRequest dto, ProductDetail entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getQuantity() != null ) {
            entity.setQuantity( dto.getQuantity() );
        }
        if ( dto.getPrice() != null ) {
            entity.setPrice( dto.getPrice() );
        }
    }

    private String entityProductName(ProductDetail productDetail) {
        if ( productDetail == null ) {
            return null;
        }
        Product product = productDetail.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String entityConfigurationName(ProductDetail productDetail) {
        if ( productDetail == null ) {
            return null;
        }
        Configuration configuration = productDetail.getConfiguration();
        if ( configuration == null ) {
            return null;
        }
        String name = configuration.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
