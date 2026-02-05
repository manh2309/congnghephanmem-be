package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.brand.BrandRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.BrandService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Brand", description = "CRUD API cho Brand (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {
    BrandService brandService;

    @GetMapping
    @Operation(summary = "Lấy danh sách brand", description = "Chỉ lấy brand chưa bị xoá")
    public ApiResponse<Object> getAllBrands(@RequestParam(required = false) String searchKey, Pageable pageable) {
        return brandService.findAll(searchKey, pageable);
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy danh sách brand (bao gồm đã xoá)", description = "Admin có thể xem tất cả brands")
    public ApiResponse<Object> getAllIncludingDeleted(Pageable pageable) {
        return brandService.findAllIncludingDeleted(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy brand theo ID", description = "Chỉ lấy brand chưa xoá")
    public ApiResponse<Object> getBrandById(@PathVariable Long id) {
        return brandService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Tạo brand mới")
    public ApiResponse<Object> createBrand(@RequestBody BrandRequest request) {
        return brandService.save(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật một phần brand")
    public ApiResponse<Object> patchBrand(
            @PathVariable Long id,
            @RequestBody BrandRequest brandPatch) {
        return brandService.patch(id, brandPatch);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá mềm brand", description = "Soft delete bằng cách set deleted_at")
    public ApiResponse<Object> deleteBrand(@PathVariable Long id) {
        brandService.softDelete(id);
        return brandService.softDelete(id);
    }

    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục brand", description = "Gán lại deleted_at = NULL để khôi phục")
    public ApiResponse<Object> restoreBrand(@PathVariable Long id) {
        return brandService.restore(id);
    }

}
