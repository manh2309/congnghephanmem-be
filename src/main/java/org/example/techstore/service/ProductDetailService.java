package org.example.techstore.service;

import org.example.techstore.dto.request.product.ProductDetailRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductDetailService {

    ApiResponse<Object> findAll(String searchKey, Long categoryId, Long brandId, Pageable pageable);

    ApiResponse<Object> findAllIncludingDeleted(Pageable pageable);

    ApiResponse<Object> save(ProductDetailRequest request);

    ApiResponse<Object> findById(Long id);

    ApiResponse<Object> patch(Long id, ProductDetailRequest updatedProduct);

    ApiResponse<Object> softDelete(Long id);

    ApiResponse<Object> restore(Long id);

    ApiResponse<Object> getByProductId(Long productId);
}
