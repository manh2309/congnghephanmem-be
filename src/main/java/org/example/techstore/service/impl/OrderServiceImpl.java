package org.example.techstore.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.text.StringEscapeUtils;
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
import org.example.techstore.enums.PaymentMethod;
import org.example.techstore.exception.AppException;
import org.example.techstore.exception.StatusCode;
import org.example.techstore.repository.AccountRepository;
import org.example.techstore.repository.OrderDetailRepository;
import org.example.techstore.repository.OrderRepository;
import org.example.techstore.repository.ProductDetailRepository;
import org.example.techstore.service.OrderService;
import org.example.techstore.utils.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    ProductDetailRepository productDetailRepository;
    TelegramService telegramService;
    private final AccountRepository accountRepository;

    private String generateOrderCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }

    @Override
    public ApiResponse<Object> getAllOrders() {

        List<Order> orders = orderRepository.findAll();

        return ApiResponse.builder()
                .message("Lấy danh sách đơn hàng thành công")
                .result(orders)
                .build();
    }

    @Override
    public ApiResponse<Object> getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.ORDER))));

        return ApiResponse.builder()
                .message("Lấy thông tin đơn hàng thành công")
                .result(order)
                .build();
    }

    @Override
    public ApiResponse<Object> getOrdersByAccount(Long accountId) {

        List<Order> orders =
                orderRepository.findByAccountIdNotDeleted(accountId);

        return ApiResponse.builder()
                .message("Lấy danh sách đơn hàng của tài khoản thành công")
                .result(orders)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Object> createOrder(OrderRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.ACCOUNT)
                        )
                ));

        Order order = new Order();
        order.setOrderCode(generateOrderCode());
        order.setStatus(OrderStatus.fromCode(request.getStatus()));
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setPaymentMethod(PaymentMethod.fromCode(request.getPaymentMethod()));
        order.setNote(request.getNote());

        order.setVat(BigDecimal.ZERO);
        order.setTotalPrice(BigDecimal.ZERO);
        order.setAccount(account);
        order.setCreatedDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        List<OrderDetailRequest> orderDetailsRequest = request.getOrderDetails();

        BigDecimal calculatedTotal = BigDecimal.ZERO;
        StringBuilder productList = new StringBuilder();

        if (orderDetailsRequest != null) {

            for (OrderDetailRequest detailReq : orderDetailsRequest) {

                ProductDetailRequest pdReq = detailReq.getProductDetail();

                ProductDetail productDetail =
                        productDetailRepository
                                .findProductDetailByConfig(
                                        pdReq.getProductId(),
                                        pdReq.getConfigurationId()
                                )
                                .orElseThrow(() ->
                                        new AppException(StatusCode.BAD_REQUEST.withMessage("Không tìm thấy thông tin chi tiết sản phẩm"))
                                );

                if (productDetail.getQuantity() < detailReq.getQuantity()) {
                    throw new AppException(StatusCode.BAD_REQUEST.withMessage(
                            "Sản phẩm '" + productDetail.getProduct().getName() + "' cấu hình này không đủ số lượng trong kho! " +
                                    "Kho hiện chỉ còn " + productDetail.getQuantity() + " chiếc."
                    ));
                }

                // Trừ số lượng kho dưới Database
                productDetail.setQuantity(productDetail.getQuantity() - detailReq.getQuantity());
                productDetailRepository.save(productDetail);
                // ==========================================

                OrderDetail detail = new OrderDetail();

                detail.setOrder(savedOrder);
                detail.setProductDetail(productDetail);
                detail.setQuantity(detailReq.getQuantity());
                detail.setUnitPrice(detailReq.getUnitPrice());

                orderDetailRepository.save(detail);

                BigDecimal lineTotal =
                        detailReq.getUnitPrice()
                                .multiply(BigDecimal.valueOf(detailReq.getQuantity()));

                calculatedTotal = calculatedTotal.add(lineTotal);

                productList.append(productDetail.getProduct().getName())
                        .append(" (x").append(detailReq.getQuantity()).append(")\n");
            }
        }

        BigDecimal vatAmount = calculatedTotal.multiply(new BigDecimal("0.1"));
        BigDecimal finalTotal = calculatedTotal.add(vatAmount);

        savedOrder.setVat(vatAmount);
        savedOrder.setTotalPrice(finalTotal);

        orderRepository.save(savedOrder);

        sendTelegram(savedOrder, productList.toString());

        return ApiResponse.builder()
                .message("Tạo đơn hàng thành công")
                .result(savedOrder)
                .build();
    }

    @Override
    public ApiResponse<Object> patchOrder(Long id, OrderRequest request) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.ORDER)
                        )
                ));

        if (request.getCustomerName() != null) {
            order.setCustomerName(request.getCustomerName());
        }

        if (request.getCustomerPhone() != null) {
            order.setCustomerPhone(request.getCustomerPhone());
        }

        if (request.getCustomerAddress() != null) {
            order.setCustomerAddress(request.getCustomerAddress());
        }

        if (request.getPaymentMethod() != null) {
            PaymentMethod method = PaymentMethod.fromCode(request.getPaymentMethod());
            order.setPaymentMethod(method);
        }

        if (request.getNote() != null) {
            order.setNote(request.getNote());
        }

        if (request.getStatus() != null) {
            OrderStatus status = OrderStatus.fromCode(request.getStatus());
            order.setStatus(status);
        }

        orderRepository.save(order);

        return ApiResponse.builder()
                .message("Cập nhật thông tin đơn hàng thành công")
                .result(order)
                .build();
    }

    @Override
    @Transactional // THÊM CÁI NÀY VÀO ĐỂ BẢO VỆ DB LÚC HOÀN KHO
    public ApiResponse<Object> updateOrderStatus(Long id, OrderStatusDTO request) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.ORDER)
                        )
                ));

        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = OrderStatus.fromCode(request.getStatus());

        boolean validTransition = switch (currentStatus) {
            case PENDING -> (newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELED);
            case CONFIRMED -> (newStatus == OrderStatus.PENDING || newStatus == OrderStatus.SHIPPING);
            case SHIPPING -> (newStatus == OrderStatus.DELIVERED);
            case DELIVERED, CANCELED -> false;
        };

        if (!validTransition) {
            throw new AppException(
                    StatusCode.BAD_REQUEST.withMessage("Không thể chuyển trạng thái đơn hàng không hợp lệ")
            );
        }

        if (newStatus == OrderStatus.CANCELED && currentStatus != OrderStatus.CANCELED) {
            List<OrderDetail> orderDetails = order.getOrderDetails();
            if (orderDetails != null) {
                for (OrderDetail detail : orderDetails) {
                    ProductDetail productDetail = detail.getProductDetail();
                    // Cộng trả lại số lượng khách đã đặt vào kho (Restock)
                    productDetail.setQuantity(productDetail.getQuantity() + detail.getQuantity());
                    productDetailRepository.save(productDetail);
                }
            }
        }
        // ==========================================

        order.setStatus(newStatus);
        orderRepository.save(order);

        sendStatusUpdateTelegram(order);

        return ApiResponse.builder()
                .message("Cập nhật trạng thái đơn hàng thành công")
                .result(order)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Object> cancelOrderByUser(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage("Không tìm thấy đơn hàng")
                ));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new AppException(
                    StatusCode.BAD_REQUEST.withMessage("Chỉ có thể hủy đơn hàng khi đang ở trạng thái Chờ xử lý")
            );
        }
        List<OrderDetail> orderDetails = order.getOrderDetails();
        if (orderDetails != null) {
            for (OrderDetail detail : orderDetails) {
                ProductDetail productDetail = detail.getProductDetail();
                productDetail.setQuantity(productDetail.getQuantity() + detail.getQuantity());
                productDetailRepository.save(productDetail);
            }
        }

        // Đổi trạng thái thành CANCELED
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        sendStatusUpdateTelegram(order);
        

        return ApiResponse.builder()
                .message("Hủy đơn hàng thành công")
                .result(order)
                .build();
    }

    private void sendTelegram(Order order, String productList) {
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String totalText = currencyFormat.format(order.getTotalPrice()) + " ₫";
        String vatText = currencyFormat.format(order.getVat()) + " ₫";
        String safeNote = (order.getNote() == null || order.getNote().isEmpty())
                ? "Không có"
                : StringEscapeUtils.escapeHtml4(order.getNote());

        // Đã sửa lại template nhìn cực kỳ chuyên nghiệp và sang xịn mịn
        String message = """
            🆕 <b>ĐƠN HÀNG MỚI TỪ TECHSTORE</b> 🆕
            
            📦 <b>Mã đơn:</b> #%s
            👤 <b>Khách hàng:</b> %s
            📞 <b>SĐT:</b> %s
            📍 <b>Địa chỉ:</b> %s
            💳 <b>Thanh toán:</b> %s
            
            🛒 <b>Chi tiết sản phẩm:</b>
            <pre>%s</pre>
            📝 <b>Ghi chú:</b> <i>%s</i>
            
            💵 <b>Tổng thanh toán:</b> <b>%s</b> <i>(Gồm VAT: %s)</i>
            """.formatted(
                order.getOrderCode(),
                order.getCustomerName(),
                order.getCustomerPhone(),
                order.getCustomerAddress(),
                order.getPaymentMethod(),
                productList.trim(),
                safeNote,
                totalText,
                vatText
        );

        telegramService.sendOrderMessage(message);
    }

    private void sendStatusUpdateTelegram(Order order) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String statusText = translateStatus(order.getStatus());

        // Template cho việc chuyển trạng thái đơn hàng
        String message = """
            🔄 <b>CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG</b> 🔄
            
            📦 <b>Mã đơn:</b> #%s
            👤 <b>Khách hàng:</b> %s
            📊 <b>Trạng thái mới:</b> <b>%s</b>
            
            ⏰ <i>Thời gian cập nhật: %s</i>
            """.formatted(
                order.getOrderCode(),
                order.getCustomerName(),
                statusText,
                time
        );

        telegramService.sendOrderMessage(message);
    }

    // Helper: Dịch trạng thái sang Tiếng Việt kèm Emoji cho dễ nhìn
    private String translateStatus(OrderStatus status) {
        return switch (status) {
            case PENDING -> "⏳ Chờ xác nhận";
            case CONFIRMED -> "✅ Đã xác nhận (Đang chuẩn bị hàng)";
            case SHIPPING -> "🚚 Đang giao hàng";
            case DELIVERED -> "🎉 Giao hàng thành công";
            case CANCELED -> "❌ Đã hủy (Đã hoàn kho)";
            default -> "❓ Không xác định";
        };
    }
}
