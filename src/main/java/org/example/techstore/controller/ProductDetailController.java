package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.techstore.dto.request.product.ProductDetailRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.ProductDetailService;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@Tag(name = "Product-Detail", description = "CRUD API cho Product-Detail (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/product-details")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    @GetMapping
    @Operation(summary = "Lấy danh sách product detail", description = "Chỉ lấy dữ liệu chưa bị xoá")
    public ApiResponse<Object> getAllProductDetails(
            @RequestParam(required = false) String searchKey,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {
        return productDetailService.findAll(searchKey, pageable);
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy danh sách product detail (bao gồm đã xoá)")
    public ApiResponse<Object> getAllIncludingDeleted(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {
        return productDetailService.findAllIncludingDeleted(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy product detail theo ID")
    public ApiResponse<Object> getProductDetailById(@PathVariable Long id) {
        return productDetailService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Tạo product detail mới")
    public ApiResponse<Object> createProductDetail(@RequestBody ProductDetailRequest request) {
        return productDetailService.save(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật product detail")
    public ApiResponse<Object> updateProductDetail(
            @PathVariable Long id,
            @RequestBody ProductDetailRequest request
    ) {
        return productDetailService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá mềm product detail")
    public ApiResponse<Object> deleteProductDetail(@PathVariable Long id) {
        return productDetailService.softDelete(id);
    }

    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục product detail")
    public ApiResponse<Object> restoreProductDetail(@PathVariable Long id) {
        return productDetailService.restore(id);
    }
}
