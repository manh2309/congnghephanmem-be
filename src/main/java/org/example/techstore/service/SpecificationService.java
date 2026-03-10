package org.example.techstore.service;

import org.example.techstore.dto.request.product.ProductRequest;
import org.example.techstore.dto.request.specifications.SpecificationRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface SpecificationService {
    ApiResponse<Object> findAll(String searchKey, Pageable pageable);

    ApiResponse<Object>  findAllIncludingDeleted(Pageable pageable);

    ApiResponse<Object>  save(SpecificationRequest request);

    ApiResponse<Object>  findById(Long id);

    ApiResponse<Object> patch(Long id, SpecificationRequest updatedSpecification);
    ApiResponse<Object>  softDelete(Long id);

    ApiResponse<Object>  restore(Long id);
}
