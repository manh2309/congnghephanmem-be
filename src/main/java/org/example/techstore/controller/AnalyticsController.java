package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.AiAnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Analytics & AI", description = "API Thống kê Doanh thu và Dự báo AI")
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnalyticsController {
    AiAnalyticsService aiAnalyticsService;

    @GetMapping("/smart-inventory")
    @Operation(summary = "Lấy báo cáo AI dự báo nhập kho (Có lưu lịch sử)",
            description = "Kết hợp WMA (Lớp 1) và Google Gemini (Lớp 2). Dữ liệu trả về được gói trong ApiResponse chuẩn.")
    public ApiResponse<Map<String, Object>> getSmartInventoryReport() {

        // Tạo hộp ApiResponse và nạp Data thô từ tầng Service vào thuộc tính 'result'
        return ApiResponse.<Map<String, Object>>builder()
                .code(1000)
                .message("Phân tích AI và tính toán dự báo thành công")
                .result(aiAnalyticsService.generateSmartInventoryReport())
                .build();

    }
}
