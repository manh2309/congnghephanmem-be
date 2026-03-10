package org.example.techstore.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.techstore.dto.request.product.ProductRequest;
import org.example.techstore.dto.response.ProductResponse;
import org.example.techstore.entity.Image;
import org.example.techstore.entity.Product;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-10T11:43:10+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Product.ProductBuilder<?, ?> product = Product.builder();

        product.productCode( dto.getProductCode() );
        product.name( dto.getName() );
        product.totalQuantity( dto.getTotalQuantity() );
        product.brand( dto.getBrand() );
        product.category( dto.getCategory() );
        product.description( dto.getDescription() );
        List<Image> list = dto.getImages();
        if ( list != null ) {
            product.images( new ArrayList<Image>( list ) );
        }

        return product.build();
    }

    @Override
    public ProductResponse toResponse(Product entity) {
        if ( entity == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.id( entity.getId() );
        productResponse.productCode( entity.getProductCode() );
        productResponse.name( entity.getName() );
        productResponse.totalQuantity( entity.getTotalQuantity() );
        productResponse.brand( entity.getBrand() );
        productResponse.category( entity.getCategory() );
        productResponse.description( entity.getDescription() );
        List<Image> list = entity.getImages();
        if ( list != null ) {
            productResponse.images( new ArrayList<Image>( list ) );
        }

        return productResponse.build();
    }

    @Override
    public List<ProductResponse> toResponseList(List<Product> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProductResponse> list = new ArrayList<ProductResponse>( entities.size() );
        for ( Product product : entities ) {
            list.add( toResponse( product ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(ProductRequest dto, Product entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getProductCode() != null ) {
            entity.setProductCode( dto.getProductCode() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getTotalQuantity() != null ) {
            entity.setTotalQuantity( dto.getTotalQuantity() );
        }
        if ( dto.getBrand() != null ) {
            entity.setBrand( dto.getBrand() );
        }
        if ( dto.getCategory() != null ) {
            entity.setCategory( dto.getCategory() );
        }
        if ( dto.getDescription() != null ) {
            entity.setDescription( dto.getDescription() );
        }
        if ( entity.getImages() != null ) {
            List<Image> list = dto.getImages();
            if ( list != null ) {
                entity.getImages().clear();
                entity.getImages().addAll( list );
            }
        }
        else {
            List<Image> list = dto.getImages();
            if ( list != null ) {
                entity.setImages( new ArrayList<Image>( list ) );
            }
        }
    }
}
