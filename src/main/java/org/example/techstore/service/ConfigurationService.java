package org.example.techstore.service;

import org.example.techstore.dto.request.configcuration.ConfigurationRequest;
import org.example.techstore.dto.response.ApiResponse;

public interface ConfigurationService {
    ApiResponse<Object> getAll();
    ApiResponse<Object> getAllActive();
    ApiResponse<Object> getDeleted();
    ApiResponse<Object> getAnyById(Long id);
    ApiResponse<Object> getById(Long id);
    ApiResponse<Object> create(ConfigurationRequest request);
    ApiResponse<Object> update(Long id, ConfigurationRequest request);
    ApiResponse<Object> deactivate(Long id);
    ApiResponse<Object> restore(Long id);
}
