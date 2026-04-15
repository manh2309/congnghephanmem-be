package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.order.OrderRequest;
import org.example.techstore.dto.request.order.OrderStatusDTO;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.OrderService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Orders")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @GetMapping
    @Operation(summary = "Lấy danh sách order")
    public ApiResponse<Object> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy order theo id")
    public ApiResponse<Object> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Lấy order theo account")
    public ApiResponse<Object> getOrdersByAccount(@PathVariable Long accountId) {
        return orderService.getOrdersByAccount(accountId);
    }

    @PostMapping
    @Operation(summary = "Tạo order")
    public ApiResponse<Object> createOrder(@RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật order")
    public ApiResponse<Object> patchOrder(
            @PathVariable Long id,
            @RequestBody OrderRequest request
    ) {
        return orderService.patchOrder(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Cập nhật trạng thái order")
    public ApiResponse<Object> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderStatusDTO request
    ) {
        return orderService.updateOrderStatus(id, request);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Khách hàng tự hủy đơn hàng")
    public ApiResponse<Object> cancelOrderByUser(@PathVariable("id") Long id) {
        return orderService.cancelOrderByUser(id);
    }
}