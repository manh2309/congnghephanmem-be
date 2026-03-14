package org.example.techstore.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.product.ProductDetailCreateRequest;
import org.example.techstore.dto.request.product.ProductDetailRequest;
import org.example.techstore.dto.request.product.ProductRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.dto.response.BrandResponse;
import org.example.techstore.dto.response.MetaResponse;
import org.example.techstore.dto.response.ProductResponse;
import org.example.techstore.entity.*;
import org.example.techstore.exception.AppException;
import org.example.techstore.exception.StatusCode;
import org.example.techstore.mapper.BrandMapper;
import org.example.techstore.mapper.ProductMapper;
import org.example.techstore.repository.*;
import org.example.techstore.service.ProductDetailService;
import org.example.techstore.service.ProductService;
import org.example.techstore.specification.ProductSpecification;
import org.example.techstore.utils.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    BrandRepository brandRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    ProductDetailRepository productDetailRepository;
    ConfigurationRepository configurationRepository;

    @Override
    public ApiResponse<Object> findAll(String searchKey, Pageable pageable) {
        Page<Product> page = productRepository.findAll(
                ProductSpecification.searchByKey(searchKey),
                pageable
        );

        List<ProductResponse> data = page.getContent()
                .stream()
                .map(productMapper::toResponse)
                .toList();

        MetaResponse meta = MetaResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .size(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.PRODUCT))
                .result(data)
                .meta(meta)   // meta cho FE
                .build();
    }

    @Override
    public ApiResponse<Object> findAllIncludingDeleted(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);

        List<ProductResponse> data = page.getContent()
                .stream()
                .map(productMapper::toResponse)
                .toList();

        MetaResponse meta = MetaResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .size(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.PRODUCT))
                .result(data)
                .meta(meta)   // meta cho FE
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Object> save(ProductRequest request) {
        if(productRepository.existsByName(request.getName())) {
            throw new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.EXISTS_FOUND, Constant.MODULE.PRODUCT)));
        }
        Product product = productMapper.toEntity(request);

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage("Không tìm thấy thương hiệu")));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage("Không tìm thấy danh mục")));

        product.setBrand(brand);
        product.setCategory(category);

        Product savedProduct = productRepository.saveAndFlush(product);

        if (request.getProductDetails() != null) {
            List<ProductDetail> listDetails = new ArrayList<>();
            for (ProductDetailCreateRequest detailReq : request.getProductDetails()) {
                Configuration config = configurationRepository.findById(detailReq.getConfigurationId())
                        .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage("Không tìm thấy cấu hình")));

                ProductDetail detail = new ProductDetail();
                detail.setProduct(savedProduct);
                detail.setConfiguration(config);
                detail.setQuantity(detailReq.getQuantity());
                detail.setPrice(detailReq.getPrice());
                listDetails.add(detail);
            }
            productDetailRepository.saveAll(listDetails);
        }

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.CREATE_SUCCESS, Constant.MODULE.PRODUCT))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.PRODUCT))));

        ProductResponse productResponse = productMapper.toResponse(product);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.PRODUCT))
                .result(productResponse)
                .build();
    }

    @Override
    public ApiResponse<Object> patch(Long id, ProductRequest updatedProduct) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.PRODUCT))));

        productMapper.updateEntityFromDto(updatedProduct, product);
        Brand brand = brandRepository.findById(updatedProduct.getBrandId())
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage("Không tìm thấy thương hiệu")));
        Category category = categoryRepository.findById(updatedProduct.getCategoryId())
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage("Không tìm thấy danh mục")));

        product.setBrand(brand);
        product.setCategory(category);
        productRepository.save(product);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.UPDATE_SUCCESS, Constant.MODULE.PRODUCT))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> softDelete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.PRODUCT))));
        product.setIsActive(Constant.IS_ACTIVE.INACTIVE);
        productRepository.save(product);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.DELETE_SUCCESS, Constant.MODULE.PRODUCT))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> restore(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.PRODUCT))));
        product.setIsActive(Constant.IS_ACTIVE.ACTIVE);
        productRepository.save(product);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.RESTORE_SUCCESS, Constant.MODULE.PRODUCT))
                .result(null)
                .build();
    }
}