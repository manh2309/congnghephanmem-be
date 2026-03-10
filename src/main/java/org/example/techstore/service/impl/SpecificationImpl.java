package org.example.techstore.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.product.ProductRequest;
import org.example.techstore.dto.request.specifications.SpecificationRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.dto.response.MetaResponse;
import org.example.techstore.dto.response.SpecificationResponse;
import org.example.techstore.entity.Configuration;
import org.example.techstore.entity.Specification;
import org.example.techstore.exception.AppException;
import org.example.techstore.exception.StatusCode;
import org.example.techstore.mapper.SpecificationMapper;
import org.example.techstore.repository.ConfigurationRepository;
import org.example.techstore.repository.SpecificationRepository;
import org.example.techstore.service.ProductService;
import org.example.techstore.service.SpecificationService;
import org.example.techstore.specification.SpecificationSpecification;
import org.example.techstore.utils.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpecificationImpl implements SpecificationService {



    SpecificationRepository specificationRepository;
    SpecificationMapper specificationMapper;
    ConfigurationRepository configurationRepository;

    @Override
    public ApiResponse<Object> findAll(String searchKey, Pageable pageable) {

        Page<Specification> page = specificationRepository.findAll(
                SpecificationSpecification.searchByKey(searchKey),
                pageable
        );

        List<SpecificationResponse> data = page.getContent()
                .stream()
                .map(specificationMapper::toResponse)
                .toList();

        MetaResponse meta = MetaResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .size(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.SPECIFICATION))
                .result(data)
                .meta(meta)
                .build();
    }

    @Override
    public ApiResponse<Object> findAllIncludingDeleted(Pageable pageable) {

        Page<Specification> page = specificationRepository.findAll(pageable);

        List<SpecificationResponse> data = page.getContent()
                .stream()
                .map(specificationMapper::toResponse)
                .toList();

        MetaResponse meta = MetaResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .size(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.SPECIFICATION))
                .result(data)
                .meta(meta)
                .build();
    }

    @Override
    public ApiResponse<Object> save(SpecificationRequest request) {

        Configuration configuration = configurationRepository.findById(request.getConfigurationId())
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.CONFIGURATION)
                        )
                ));

        Specification specification = specificationMapper.toEntity(request);

        specification.setConfiguration(configuration);

        specificationRepository.save(specification);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.CREATE_SUCCESS, Constant.MODULE.SPECIFICATION))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> findById(Long id) {

        Specification specification = specificationRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.SPECIFICATION)
                        )
                ));

        SpecificationResponse response = specificationMapper.toResponse(specification);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.SPECIFICATION))
                .result(response)
                .build();
    }

    @Override
    public ApiResponse<Object> patch(Long id, SpecificationRequest updatedSpecification) {

        Specification specification = specificationRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.SPECIFICATION)
                        )
                ));

        // cập nhật dữ liệu từ request vào entity
        specificationMapper.updateEntityFromDto(updatedSpecification, specification);

        specificationRepository.save(specification);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.UPDATE_SUCCESS, Constant.MODULE.SPECIFICATION))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> softDelete(Long id) {

        Specification specification = specificationRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.SPECIFICATION)
                        )
                ));

        specification.setIsActive(Constant.IS_ACTIVE.INACTIVE);

        specificationRepository.save(specification);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.DELETE_SUCCESS, Constant.MODULE.SPECIFICATION))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> restore(Long id) {

        Specification specification = specificationRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.SPECIFICATION)
                        )
                ));

        specification.setIsActive(Constant.IS_ACTIVE.ACTIVE);

        specificationRepository.save(specification);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.RESTORE_SUCCESS, Constant.MODULE.SPECIFICATION))
                .result(null)
                .build();
    }
}
