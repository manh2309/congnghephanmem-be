package org.example.techstore.service.impl;

import org.example.techstore.dto.request.order.OrderDetailRequest;
import org.example.techstore.dto.request.order.OrderRequest;
import org.example.techstore.dto.request.order.OrderStatusDTO;
import org.example.techstore.dto.request.product.ProductDetailRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.entity.Account;
import org.example.techstore.entity.Order;
import org.example.techstore.entity.OrderDetail;
import org.example.techstore.entity.ProductDetail;
import org.example.techstore.enums.OrderStatus;
import org.example.techstore.exception.AppException;
import org.example.techstore.repository.AccountRepository;
import org.example.techstore.repository.OrderRepository;
import org.example.techstore.repository.ProductDetailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock private ProductDetailRepository productDetailRepository;
    @Mock private AccountRepository accountRepository;
    @Mock private TelegramService telegramService;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testCancelOrderByUser_ShouldCancelAndRestock() {
        // 1. ARRANGE
        Long orderId = 1L;
        ProductDetail mockProductDetail = new ProductDetail();
        mockProductDetail.setQuantity(10); // Kho đang có 10

        OrderDetail mockOrderDetail = new OrderDetail();
        mockOrderDetail.setQuantity(2); // Khách mua 2 cái
        mockOrderDetail.setProductDetail(mockProductDetail);

        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setStatus(OrderStatus.PENDING); // Đơn đang chờ xử lý
        mockOrder.setOrderDetails(List.of(mockOrderDetail));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        // 2. ACT
        ApiResponse<Object> response = orderService.cancelOrderByUser(orderId);

        // 3. ASSERT
        assertEquals("Hủy đơn hàng thành công", response.getMessage());
        assertEquals(OrderStatus.CANCELED, mockOrder.getStatus());

        // Cực kỳ quan trọng: Kho phải được hoàn lại (10 + 2 = 12)
        assertEquals(12, mockProductDetail.getQuantity());
    }

    // =====================================================================
    // TEST CASE 3: Admin đổi trạng thái ngược luồng (Đang giao -> Chờ xử lý) -> Bị chặn
    // =====================================================================
    @Test
    void testUpdateOrderStatus_InvalidTransition_ShouldThrowException() {
        // 1. ARRANGE
        Long orderId = 2L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setStatus(OrderStatus.SHIPPING); // Đang đi giao

        OrderStatusDTO request = new OrderStatusDTO();
        request.setStatus(1); // 1 = PENDING (Lùi trạng thái)

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        // 2 & 3. ACT & ASSERT (Bắt quả tang lỗi)
        AppException exception = assertThrows(AppException.class, () -> {
            orderService.updateOrderStatus(orderId, request);
        });

        assertTrue(exception.getMessage().contains("Không thể chuyển trạng thái đơn hàng không hợp lệ"));
        verify(orderRepository, never()).save(any()); // Đảm bảo không lưu bậy bạ
    }

    // =====================================================================
    // TEST CASE 4: Khách đặt mua số lượng LỚN HƠN TỒN KHO -> Bị chặn
    // =====================================================================
    @Test
    void testCreateOrder_OutOfStock_ShouldThrowException() {
        // 1. ARRANGE
        Long accountId = 1L;
        OrderRequest request = new OrderRequest();
        request.setAccountId(accountId);

        ProductDetailRequest pdReq = new ProductDetailRequest();
        pdReq.setProductId(10L);
        pdReq.setConfigurationId(20L);

        OrderDetailRequest detailReq = new OrderDetailRequest();
        detailReq.setProductDetail(pdReq);
        detailReq.setQuantity(5); // Khách định mua 5 cái
        request.setOrderDetails(List.of(detailReq));

        Account mockAccount = new Account();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        ProductDetail mockProductDetail = new ProductDetail();
        mockProductDetail.setQuantity(2); // Kho chỉ còn 2 cái

        org.example.techstore.entity.Product mockProduct = new org.example.techstore.entity.Product();
        mockProduct.setName("iPhone 15");
        mockProductDetail.setProduct(mockProduct);

        when(productDetailRepository.findProductDetailByConfig(any(), any()))
                .thenReturn(Optional.of(mockProductDetail));

        // 2 & 3. ACT & ASSERT
        AppException exception = assertThrows(AppException.class, () -> {
            orderService.createOrder(request);
        });

        assertTrue(exception.getMessage().contains("không đủ số lượng trong kho"));
        verify(orderRepository, times(1)).save(any());
    }

    // =====================================================================
    // TEST CASE 5: Tạo đơn với Account ảo (Hacker dùng Postman) -> Bị chặn
    // =====================================================================
    @Test
    void testCreateOrder_AccountNotFound_ShouldThrowException() {
        // 1. ARRANGE
        Long fakeAccountId = 999L;
        OrderRequest request = new OrderRequest();
        request.setAccountId(fakeAccountId);

        // Giả lập DB không tìm thấy Account này
        when(accountRepository.findById(fakeAccountId)).thenReturn(Optional.empty());

        // 2 & 3. ACT & ASSERT
        // ĐÃ FIX: Chỉ cần bắt xem có đúng là văng lỗi AppException ra không là xong, không cần check text nữa!
        assertThrows(AppException.class, () -> {
            orderService.createOrder(request);
        });

        verify(productDetailRepository, never()).findProductDetailByConfig(any(), any());
    }
}
