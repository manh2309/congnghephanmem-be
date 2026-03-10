package org.example.techstore.service;

import org.example.techstore.dto.request.brand.BrandRequest;
import org.example.techstore.dto.request.product.ProductRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

public interface ProductService {
    ApiResponse<Object> findAll(String searchKey, Pageable pageable);

    ApiResponse<Object>  findAllIncludingDeleted(Pageable pageable);

    ApiResponse<Object>  save(ProductRequest request);

    ApiResponse<Object>  findById(Long id);

    ApiResponse<Object>  patch(Long id, ProductRequest updatedProduct);

    ApiResponse<Object>  softDelete(Long id);

    ApiResponse<Object>  restore(Long id);
}
