package org.example.techstore.service;

import org.example.techstore.dto.request.role.RoleCreateRequest;
import org.example.techstore.dto.request.role.RoleUpdateRequest;
import org.example.techstore.dto.response.ApiResponse;

public interface RoleService {
    ApiResponse<Object> findAll();

    ApiResponse<Object> findAllIncludingDeleted();

    ApiResponse<Object> findById(Long id);

    ApiResponse<Object> save(RoleCreateRequest request);

    ApiResponse<Object> patch(Long id, RoleUpdateRequest request);

    ApiResponse<Object> restore(Long id);

    ApiResponse<Object> softDelete(Long id);
}


