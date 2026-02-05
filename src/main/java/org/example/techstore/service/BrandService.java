package org.example.techstore.service;

import org.example.techstore.dto.request.brand.BrandRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface BrandService {
    ApiResponse<Object> findAll(String searchKey, Pageable pageable);

    ApiResponse<Object>  findAllIncludingDeleted(Pageable pageable);

    ApiResponse<Object>  save(BrandRequest request);

    ApiResponse<Object>  findById(Long id);

    ApiResponse<Object>  patch(Long id, BrandRequest updatedBrand);

    ApiResponse<Object>  softDelete(Long id);

    ApiResponse<Object>  restore(Long id);
}
