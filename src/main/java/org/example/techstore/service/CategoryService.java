package org.example.techstore.service;

import org.example.techstore.dto.request.categories.CategoryRequest;
import org.example.techstore.dto.response.ApiResponse;

public interface CategoryService {
    ApiResponse<Object> getAllActive();

    ApiResponse<Object> getAll();

    ApiResponse<Object> getById(Long id);

    ApiResponse<Object> getAnyById(Long id);

    ApiResponse<Object> create(CategoryRequest request);

    ApiResponse<Object> update(Long id, CategoryRequest request);

    ApiResponse<Object> deactivate(Long id);

    ApiResponse<Object> restore(Long id);
}
