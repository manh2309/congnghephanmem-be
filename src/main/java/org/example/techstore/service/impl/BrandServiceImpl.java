package org.example.techstore.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.brand.BrandRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.dto.response.BrandResponse;
import org.example.techstore.dto.response.MetaResponse;
import org.example.techstore.entity.Brand;
import org.example.techstore.exception.AppException;
import org.example.techstore.exception.StatusCode;
import org.example.techstore.mapper.BrandMapper;
import org.example.techstore.repository.BrandRepository;
import org.example.techstore.service.BrandService;
import org.example.techstore.specification.BrandSpecification;
import org.example.techstore.utils.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImpl implements BrandService {
    BrandRepository brandRepository;
    BrandMapper brandMapper;

    @Override
    public ApiResponse<Object> findAll(String searchKey, Pageable pageable) {
        Page<Brand> page = brandRepository.findAll(
                BrandSpecification.searchByKey(searchKey),
                pageable
        );

        List<BrandResponse> data = page.getContent()
                .stream()
                .map(brandMapper::toResponse)
                .toList();

        MetaResponse meta = MetaResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .size(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.BRAND))
                .result(data)
                .meta(meta)   // meta cho FE
                .build();
    }

    @Override
    public ApiResponse<Object> findAllIncludingDeleted(Pageable pageable) {
        Page<Brand> page = brandRepository.findAll(pageable);

        List<BrandResponse> data = page.getContent()
                .stream()
                .map(brandMapper::toResponse)
                .toList();

        MetaResponse meta = MetaResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .size(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.BRAND))
                .result(data)
                .meta(meta)   // meta cho FE
                .build();
    }

    @Transactional
    @Override
    public ApiResponse<Object> save(BrandRequest request) {
        // 1. Chỉ cần check theo tên nếu cần thiết (ví dụ không cho trùng tên Brand)
        if(brandRepository.existsByName(request.getName())) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage("Tên thương hiệu đã tồn tại"));
        }
        Brand brand = brandMapper.toEntity(request);
        brandRepository.save(brand);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.CREATE_SUCCESS, Constant.MODULE.BRAND))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> findById(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.BRAND))));

        BrandResponse brandResponse = brandMapper.toResponse(brand);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.BRAND))
                .result(brandResponse)
                .build();
    }

    @Override
    public ApiResponse<Object> patch(Long id, BrandRequest updatedBrand) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.BRAND))));

        brandMapper.updateEntityFromDto(updatedBrand, brand);
        brandRepository.save(brand);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.UPDATE_SUCCESS, Constant.MODULE.BRAND))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> softDelete(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.BRAND))));
        brand.setIsActive(Constant.IS_ACTIVE.INACTIVE);
        brandRepository.save(brand);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.DELETE_SUCCESS, Constant.MODULE.BRAND))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> restore(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.BRAND))));
        brand.setIsActive(Constant.IS_ACTIVE.ACTIVE);
        brandRepository.save(brand);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.RESTORE_SUCCESS, Constant.MODULE.BRAND))
                .result(null)
                .build();
    }
}
