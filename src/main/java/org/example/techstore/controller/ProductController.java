package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.brand.BrandRequest;
import org.example.techstore.dto.request.product.ProductRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.BrandService;
import org.example.techstore.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product", description = "CRUD API cho Product (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @GetMapping
    @Operation(summary = "Lấy danh sách product", description = "Chỉ lấy product chưa bị xoá")
    public ApiResponse<Object> getAllProducts(@RequestParam(required = false) String searchKey, @PageableDefault(
            page = 0,
            size = 10,
            sort = "id",
            direction = Sort.Direction.ASC
    )
    Pageable pageable) {
        return productService.findAll(searchKey, pageable);
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy danh sách product (bao gồm đã xoá)", description = "Admin có thể xem tất cả product")
    public ApiResponse<Object> getAllIncludingDeleted(@PageableDefault(
            page = 0,
            size = 10,
            sort = "id",
            direction = Sort.Direction.ASC
    )
                                                      Pageable pageable) {
        return productService.findAllIncludingDeleted(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy product theo ID", description = "Chỉ lấy product chưa xoá")
    public ApiResponse<Object> getProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Tạo product mới")
    public ApiResponse<Object> createProduct(@RequestBody ProductRequest request) {
        return productService.save(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật một phần product")
    public ApiResponse<Object> patchProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest productPatch) {
        return productService.patch(id, productPatch);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá mềm product", description = "Soft delete bằng cách set deleted_at")
    public ApiResponse<Object> deleteProduct(@PathVariable Long id) {
        productService.softDelete(id);
        return productService.softDelete(id);
    }

    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục product", description = "Gán lại deleted_at = NULL để khôi phục")
    public ApiResponse<Object> restoreProduct(@PathVariable Long id) {
        return productService.restore(id);
    }

}
