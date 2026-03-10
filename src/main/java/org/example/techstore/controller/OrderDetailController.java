package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.order.OrderDetailRequestDTO;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.entity.OrderDetail;
import org.example.techstore.service.OrderDetailService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order Details")
@RestController
@RequestMapping("/api/order-details")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailController {

    OrderDetailService orderDetailService;

    @GetMapping
    @Operation(summary = "Lấy danh sách order detail chưa bị xoá")
    public ApiResponse<Object> getAllActive() {
        return orderDetailService.getAllNotDeleted();
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy tất cả order detail (bao gồm đã xoá)")
    public ApiResponse<Object> getAll() {
        return orderDetailService.getAllIncludingDeleted();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy order detail theo id")
    public ApiResponse<Object> getById(@PathVariable Long id) {
        return orderDetailService.getNotDeletedById(id);
    }

    @GetMapping("/{id}/any")
    @Operation(summary = "Lấy order detail theo id (bao gồm đã xoá)")
    public ApiResponse<Object> getAnyById(@PathVariable Long id) {
        return orderDetailService.getAnyById(id);
    }

    @PostMapping
    @Operation(summary = "Tạo order detail")
    public ApiResponse<Object> create(@RequestBody OrderDetailRequestDTO request) {
        return orderDetailService.create(request);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật order detail")
    public ApiResponse<Object> update(
            @PathVariable Long id,
            @RequestBody OrderDetailRequestDTO request
    ) {
        return orderDetailService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá mềm order detail")
    public ApiResponse<Object> softDelete(@PathVariable Long id) {
        return orderDetailService.softDelete(id);
    }

    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục order detail")
    public ApiResponse<Object> restore(@PathVariable Long id) {
        return orderDetailService.restore(id);
    }

}
