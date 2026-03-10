package org.example.techstore.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.techstore.dto.request.specifications.SpecificationRequest;
import org.example.techstore.dto.response.SpecificationResponse;
import org.example.techstore.entity.Specification;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-10T11:43:10+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class SpecificationMapperImpl implements SpecificationMapper {

    @Override
    public Specification toEntity(SpecificationRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Specification.SpecificationBuilder<?, ?> specification = Specification.builder();

        specification.specificationCode( dto.getSpecificationCode() );
        specification.name( dto.getName() );
        specification.value( dto.getValue() );
        specification.configuration( dto.getConfiguration() );

        return specification.build();
    }

    @Override
    public SpecificationResponse toResponse(Specification entity) {
        if ( entity == null ) {
            return null;
        }

        SpecificationResponse.SpecificationResponseBuilder specificationResponse = SpecificationResponse.builder();

        specificationResponse.id( entity.getId() );
        specificationResponse.specificationCode( entity.getSpecificationCode() );
        specificationResponse.name( entity.getName() );
        specificationResponse.value( entity.getValue() );
        specificationResponse.configuration( entity.getConfiguration() );

        return specificationResponse.build();
    }

    @Override
    public List<SpecificationResponse> toResponseList(List<Specification> entities) {
        if ( entities == null ) {
            return null;
        }

        List<SpecificationResponse> list = new ArrayList<SpecificationResponse>( entities.size() );
        for ( Specification specification : entities ) {
            list.add( toResponse( specification ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(SpecificationRequest dto, Specification entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getSpecificationCode() != null ) {
            entity.setSpecificationCode( dto.getSpecificationCode() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getValue() != null ) {
            entity.setValue( dto.getValue() );
        }
        if ( dto.getConfiguration() != null ) {
            entity.setConfiguration( dto.getConfiguration() );
        }
    }
}
