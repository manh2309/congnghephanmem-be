package org.example.techstore.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.order.OrderDetailRequestDTO;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.entity.Order;
import org.example.techstore.entity.OrderDetail;
import org.example.techstore.entity.ProductDetail;
import org.example.techstore.repository.OrderDetailRepository;
import org.example.techstore.repository.OrderRepository;
import org.example.techstore.repository.ProductDetailRepository;
import org.example.techstore.service.OrderDetailService;
import org.example.techstore.utils.Constant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailServiceImpl implements OrderDetailService {

    OrderDetailRepository repository;
    OrderRepository orderRepository;
    ProductDetailRepository productDetailRepository;

    @Override
    public ApiResponse<Object> getAllNotDeleted() {

        List<OrderDetail> data = repository.findAllNotDeleted();

        return ApiResponse.builder()
                .message("Lấy danh sách order detail thành công")
                .result(data)
                .build();
    }

    @Override
    public ApiResponse<Object> getAllIncludingDeleted() {

        List<OrderDetail> data = repository.findAllIncludingDeleted();

        return ApiResponse.builder()
                .message("Lấy toàn bộ order detail thành công")
                .result(data)
                .build();
    }

    @Override
    public ApiResponse<Object> getNotDeletedById(Long id) {

        OrderDetail detail = repository.findNotDeletedById(id);

        if (detail == null) {
            return ApiResponse.builder()
                    .message("Không tìm thấy order detail")
                    .build();
        }

        return ApiResponse.builder()
                .message("Lấy order detail thành công")
                .result(detail)
                .build();
    }

    @Override
    public ApiResponse<Object> getAnyById(Long id) {

        OrderDetail detail = repository.findAnyById(id);

        if (detail == null) {
            return ApiResponse.builder()
                    .message("Không tìm thấy order detail")
                    .build();
        }

        return ApiResponse.builder()
                .message("Lấy order detail thành công")
                .result(detail)
                .build();
    }

    @Override
    public ApiResponse<Object> create(OrderDetailRequestDTO request) {

        Order order = orderRepository.findById(request.getOrderId()).orElse(null);
        ProductDetail productDetail = productDetailRepository.findById(request.getProductDetailId()).orElse(null);

        if (order == null || productDetail == null) {
            return ApiResponse.builder()
                    .message("Order hoặc ProductDetail không tồn tại")
                    .build();
        }

        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .order(order)
                .productDetail(productDetail)
                .build();

        repository.save(orderDetail);

        return ApiResponse.builder()
                .message("Tạo order detail thành công")
                .result(orderDetail)
                .build();
    }

    @Override
    public ApiResponse<Object> update(Long id, OrderDetailRequestDTO request) {

        OrderDetail existing = repository.findAnyById(id);

        if (existing == null) {
            return ApiResponse.builder()
                    .message("Không tìm thấy order detail")
                    .build();
        }

        Order order = orderRepository.findById(request.getOrderId()).orElse(null);
        ProductDetail productDetail = productDetailRepository.findById(request.getProductDetailId()).orElse(null);

        if (order == null || productDetail == null) {
            return ApiResponse.builder()
                    .message("Order hoặc ProductDetail không tồn tại")
                    .build();
        }

        existing.setQuantity(request.getQuantity());
        existing.setUnitPrice(request.getUnitPrice());
        existing.setOrder(order);
        existing.setProductDetail(productDetail);

        repository.save(existing);

        return ApiResponse.builder()
                .message("Cập nhật order detail thành công")
                .result(existing)
                .build();
    }

    @Override
    public ApiResponse<Object> softDelete(Long id) {

        OrderDetail existing = repository.findNotDeletedById(id);

        if (existing == null) {
            return ApiResponse.builder()
                    .message("Order detail không tồn tại")
                    .build();
        }

        existing.setIsActive(Constant.IS_ACTIVE.INACTIVE);
        repository.save(existing);

        return ApiResponse.builder()
                .message("Xóa mềm order detail thành công")
                .build();
    }

    @Override
    public ApiResponse<Object> restore(Long id) {

        OrderDetail existing = repository.findAnyById(id);

        if (existing == null) {
            return ApiResponse.builder()
                    .message("Order detail không tồn tại")
                    .build();
        }

        existing.setIsActive(Constant.IS_ACTIVE.ACTIVE);
        repository.save(existing);

        return ApiResponse.builder()
                .message("Khôi phục order detail thành công")
                .build();
    }
}
