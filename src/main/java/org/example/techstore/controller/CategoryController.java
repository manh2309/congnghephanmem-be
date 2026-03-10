package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.techstore.dto.request.categories.CategoryRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.CategoryService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Category", description = "CRUD API cho Category (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Lấy tất cả category đang active
    @GetMapping
    @Operation(summary = "Lấy danh sách category đang hoạt động")
    public ApiResponse<Object> getAllActive() {
        return categoryService.getAllActive();
    }

    // Lấy tất cả category kể cả inactive
    @GetMapping("/all")
    @Operation(summary = "Lấy tất cả category (bao gồm inactive)")
    public ApiResponse<Object> getAll() {
        return categoryService.getAll();
    }

    // Lấy category theo id (active)
    @GetMapping("/{id}")
    @Operation(summary = "Lấy category theo ID")
    public ApiResponse<Object> getById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    // Lấy category theo id kể cả inactive
    @GetMapping("/{id}/any")
    @Operation(summary = "Lấy category theo ID (kể cả inactive)")
    public ApiResponse<Object> getAnyById(@PathVariable Long id) {
        return categoryService.getAnyById(id);
    }

    // Tạo category
    @PostMapping
    @Operation(summary = "Tạo category mới")
    public ApiResponse<Object> create(@RequestBody CategoryRequest request) {
        return categoryService.create(request);
    }

    // Update category
    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật category")
    public ApiResponse<Object> update(
            @PathVariable Long id,
            @RequestBody CategoryRequest request
    ) {
        return categoryService.update(id, request);
    }

    // Soft delete (is_active = false)
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa mềm category (is_active = false)")
    public ApiResponse<Object> deactivate(@PathVariable Long id) {
        return categoryService.deactivate(id);
    }

    // Restore (is_active = true)
    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục category (is_active = true)")
    public ApiResponse<Object> restore(@PathVariable Long id) {
        return categoryService.restore(id);
    }

}