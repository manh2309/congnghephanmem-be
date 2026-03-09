package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.role.RoleCreateRequest;
import org.example.techstore.dto.request.role.RoleUpdateRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.impl.RoleServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;

@Tag(name = "Role", description = "CRUD API cho Role (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleServiceImpl roleService;

    // Lấy tất cả roles chưa xoá
    @GetMapping
    @Operation(summary = "Lấy danh sách role", description = "Chỉ lấy role chưa bị xoá")
    public ApiResponse<Object> getAllRoles() {
        return roleService.findAll();
    }

    // Lấy tất cả roles kể cả đã xoá
    @GetMapping("/all")
    @Operation(summary = "Lấy danh sách role (bao gồm đã xoá)", description = "Admin có thể xem tất cả roles")
    public ApiResponse<Object> getAllIncludingDeleted() {
        return roleService.findAllIncludingDeleted();
    }

    // Lấy 1 role theo id
    @GetMapping("/{id}")
    @Operation(summary = "Lấy role theo ID", description = "Chỉ lấy role chưa xoá")
    public ApiResponse<Object> getRoleById(@PathVariable Long id) {
        return roleService.findById(id);
    }

    // Tạo role mới
    @PostMapping
    @Operation(summary = "Tạo role mới")
    public ApiResponse<Object> createRole(@RequestBody RoleCreateRequest request) {
        return roleService.save(request);
    }
    // Update role
    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật một phần role")
    public ApiResponse<Object> patchRole(
            @PathVariable Long id,
            @RequestBody RoleUpdateRequest request) {

        return roleService.patch(id, request);
    }

    // Restore role
    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục role", description = "Gán lại deleted_at = NULL để khôi phục")
    public ApiResponse<Object> restoreRole(@PathVariable Long id) {
        return roleService.restore(id);
    }
    // Soft delete role
        @DeleteMapping("/{id}")
        @Operation(summary = "Xoá mềm role", description = "Soft delete bằng cách set deleted_at")
        public ApiResponse<Object> deleteRole(@PathVariable Long id) {
            return roleService.softDelete(id);
        }
}
