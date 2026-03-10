package org.example.techstore.service;

import org.example.techstore.dto.request.order.OrderDetailRequestDTO;
import org.example.techstore.dto.response.ApiResponse;

public interface OrderDetailService {

    ApiResponse<Object> getAllNotDeleted();

    ApiResponse<Object> getAllIncludingDeleted();

    ApiResponse<Object> getNotDeletedById(Long id);

    ApiResponse<Object> getAnyById(Long id);

    ApiResponse<Object> create(OrderDetailRequestDTO request);

    ApiResponse<Object> update(Long id, OrderDetailRequestDTO request);

    ApiResponse<Object> softDelete(Long id);

    ApiResponse<Object> restore(Long id);
}
