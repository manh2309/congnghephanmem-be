package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.configcuration.ConfigurationRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.ConfigurationService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Configurations", description = "CRUD API cho Configurations (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/configurations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigurationController {

    ConfigurationService configurationService;

    @GetMapping("/all")
    @Operation(summary = "Lấy tất cả Configuration", description = "Lấy toàn bộ danh sách kể cả đã vô hiệu hóa")
    public ApiResponse<Object> getAll() {
        return configurationService.getAll();
    }

    @GetMapping
    @Operation(summary = "Lấy Configuration đang hoạt động")
    public ApiResponse<Object> getActive() {
        return configurationService.getAllActive();
    }

    @GetMapping("/deleted")
    @Operation(summary = "Lấy Configuration đã vô hiệu hóa")
    public ApiResponse<Object> getDeleted() {
        return configurationService.getDeleted();
    }

    @GetMapping("/any/{id}")
    @Operation(summary = "Lấy Configuration bất kỳ theo ID")
    public ApiResponse<Object> getById(@PathVariable Long id) {
        return configurationService.getAnyById(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy Configuration đang hoạt động theo ID")
    public ApiResponse<Object> getActiveById(@PathVariable Long id) {
        return configurationService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Thêm mới Configuration")
    public ApiResponse<Object> create(@RequestBody ConfigurationRequest config) {
        // Logic xử lý lỗi đã được đẩy ra GlobalExceptionHandler
        return configurationService.create(config);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật một phần Configuration")
    public ApiResponse<Object> update(@PathVariable Long id, @RequestBody ConfigurationRequest configuration) {
        return configurationService.update(id, configuration);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Vô hiệu hóa (Xóa mềm) Configuration")
    public ApiResponse<Object> deactivate(@PathVariable Long id) {
        return configurationService.deactivate(id);
    }

    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục Configuration")
    public ApiResponse<Object> restore(@PathVariable Long id) {
        return configurationService.restore(id);
    }
}