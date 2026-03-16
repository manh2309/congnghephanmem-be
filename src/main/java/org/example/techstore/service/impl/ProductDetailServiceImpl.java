package org.example.techstore.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.product.ProductDetailRequest;
import org.example.techstore.dto.request.product.ProductRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.dto.response.MetaResponse;
import org.example.techstore.dto.response.ProductDetailResponse;
import org.example.techstore.dto.response.ProductResponse;
import org.example.techstore.entity.Configuration;
import org.example.techstore.entity.Product;
import org.example.techstore.entity.ProductDetail;
import org.example.techstore.exception.AppException;
import org.example.techstore.exception.StatusCode;
import org.example.techstore.mapper.ProductDetailMapper;
import org.example.techstore.mapper.ProductMapper;
import org.example.techstore.repository.ConfigurationRepository;
import org.example.techstore.repository.ProductDetailRepository;
import org.example.techstore.repository.ProductRepository;
import org.example.techstore.service.ProductDetailService;
import org.example.techstore.specification.ProductDetailSpecification;
import org.example.techstore.specification.ProductSpecification;
import org.example.techstore.utils.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductDetailServiceImpl implements ProductDetailService {

    ProductDetailRepository productDetailRepository;
    ProductRepository productRepository;
    ConfigurationRepository configurationRepository;
    ProductDetailMapper productDetailMapper;



    @Override
    public ApiResponse<Object> findAll(String searchKey, Long categoryId, Long brandId, Pageable pageable) {

        Page<ProductDetail> page = productDetailRepository.findAll(
                ProductDetailSpecification.filter(searchKey, categoryId, brandId),
                pageable
        );

        List<Map<String, Object>> data = page.getContent()
                .stream()
                .map(pd -> {

                    ProductDetailResponse dto = productDetailMapper.toResponse(pd);

                    Map<String, Object> map = new HashMap<>();

                    map.put("productDetail", dto);
                    map.put("product", pd.getProduct());
                    map.put("brand", pd.getProduct() != null ? pd.getProduct().getBrand() : null);
                    map.put("category", pd.getProduct() != null ? pd.getProduct().getCategory() : null);
                    map.put("images", pd.getProduct() != null ? pd.getProduct().getImages() : null);
                    map.put("configuration", pd.getConfiguration());
                    map.put("specifications", pd.getConfiguration() != null ? pd.getConfiguration().getSpecifications() : null);

                    return map;

                })
                .toList();

        MetaResponse meta = MetaResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .size(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.PRODUCT_DETAIL))
                .result(data)
                .meta(meta)
                .build();
    }

    @Override
    public ApiResponse<Object> findAllIncludingDeleted(Pageable pageable) {

        Page<ProductDetail> page = productDetailRepository.findAll(pageable);

        List<ProductDetailResponse> data = page.getContent()
                .stream()
                .map(productDetailMapper::toResponse)
                .toList();

        MetaResponse meta = MetaResponse.builder()
                .page(pageable.getPageNumber() + 1)
                .size(pageable.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.PRODUCT_DETAIL))
                .result(data)
                .meta(meta)
                .build();
    }

    @Override
    public ApiResponse<Object> save(ProductDetailRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage("Không tìm thấy sản phẩm")
                ));

        Configuration configuration = configurationRepository.findById(request.getConfigurationId())
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage("Không tìm thấy cấu hình")
                ));

        ProductDetail productDetail = productDetailMapper.toEntity(request);

        productDetail.setProduct(product);
        productDetail.setConfiguration(configuration);
        productDetail.setIsActive(Constant.IS_ACTIVE.ACTIVE);

        productDetailRepository.save(productDetail);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.CREATE_SUCCESS, Constant.MODULE.PRODUCT_DETAIL))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> findById(Long id) {

        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.PRODUCT_DETAIL)
                        )
                ));

        ProductDetailResponse response = productDetailMapper.toResponse(productDetail);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.PRODUCT_DETAIL))
                .result(response)
                .build();
    }

    @Override
    public ApiResponse<Object> patch(Long id, ProductDetailRequest updatedProduct) {

        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.PRODUCT_DETAIL)
                        )
                ));

        Product product = productRepository.findById(updatedProduct.getProductId())
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage("Không tìm thấy sản phẩm")
                ));

        Configuration configuration = configurationRepository.findById(updatedProduct.getConfigurationId())
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage("Không tìm thấy cấu hình")
                ));

        productDetailMapper.updateEntityFromDto(updatedProduct, productDetail);

        productDetail.setProduct(product);
        productDetail.setConfiguration(configuration);

        productDetailRepository.save(productDetail);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.UPDATE_SUCCESS, Constant.MODULE.PRODUCT_DETAIL))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> softDelete(Long id) {

        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.PRODUCT_DETAIL)
                        )
                ));

        productDetail.setIsActive(Constant.IS_ACTIVE.INACTIVE);

        productDetailRepository.save(productDetail);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.DELETE_SUCCESS, Constant.MODULE.PRODUCT_DETAIL))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> restore(Long id) {

        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.PRODUCT_DETAIL)
                        )
                ));

        productDetail.setIsActive(Constant.IS_ACTIVE.ACTIVE);

        productDetailRepository.save(productDetail);

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.RESTORE_SUCCESS, Constant.MODULE.PRODUCT_DETAIL))
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> getByProductId(Long productId) {
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.RESTORE_SUCCESS, Constant.MODULE.PRODUCT_DETAIL))
                .result(productDetailRepository.findByProductId(productId))
                .build();
    }

}
