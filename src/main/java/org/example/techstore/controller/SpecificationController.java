package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.techstore.dto.request.specifications.SpecificationRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.SpecificationService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Specification", description = "CRUD API cho Specification (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/specifications")
@RequiredArgsConstructor
public class SpecificationController {

    private final SpecificationService specificationService;

    @GetMapping
    @Operation(summary = "Lấy danh sách specification", description = "Chỉ lấy dữ liệu chưa bị xoá")
    public ApiResponse<Object> getAllSpecifications(
            @RequestParam(required = false) String searchKey,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {
        return specificationService.findAll(searchKey, pageable);
    }

    @GetMapping("/all")
    @Operation(summary = "Lấy danh sách specification (bao gồm đã xoá)")
    public ApiResponse<Object> getAllIncludingDeleted(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {
        return specificationService.findAllIncludingDeleted(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy specification theo ID")
    public ApiResponse<Object> getSpecificationById(@PathVariable Long id) {
        return specificationService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Tạo specification mới")
    public ApiResponse<Object> createSpecification(@RequestBody SpecificationRequest request) {
        return specificationService.save(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật specification")
    public ApiResponse<Object> updateSpecification(
            @PathVariable Long id,
            @RequestBody SpecificationRequest request
    ) {
        return specificationService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá mềm specification")
    public ApiResponse<Object> deleteSpecification(@PathVariable Long id) {
        return specificationService.softDelete(id);
    }

    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục specification")
    public ApiResponse<Object> restoreSpecification(@PathVariable Long id) {
        return specificationService.restore(id);
    }
}
