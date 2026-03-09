package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.account.CreateAccountRequest;
import org.example.techstore.dto.request.account.PatchAccountRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.AccountService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account", description = "CRUD API cho User (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {

    AccountService accountService;

    // 1. Lấy danh sách user chưa xoá
    @GetMapping
    @Operation(summary = "Lấy danh sách user", description = "Chỉ lấy user chưa bị xoá")
    public ApiResponse<Object> getAllUsers() {

        return accountService.getAllAccounts();
    }

    // 2. Thống kê user role = customer
    @GetMapping("/statistics")
    @Operation(summary = "Thống kê người dùng", description = "Thống kê user có role khách hàng")
    public ApiResponse<Object> getUserStatistics() {

        return accountService.getUsersRole2Statistics();
    }

    // 3. Lấy tất cả user kể cả đã xoá
    @GetMapping("/all")
    @Operation(summary = "Lấy danh sách user (bao gồm đã xoá)", description = "Admin có thể xem tất cả users")
    public ApiResponse<Object> getAllUsersIncludingDeleted() {

        return accountService.getAllAccountsIncludingInactive();
    }

    // 4. Lấy user theo ID
    @GetMapping("/{id}")
    @Operation(summary = "Lấy user theo ID")
    public ApiResponse<Object> getUserById(@PathVariable Long id) {

        return accountService.getAccountById(id);
    }

    // 5. Tạo user
    @PostMapping
    @Operation(summary = "Tạo user mới")
    public ApiResponse<Object> createUser(@RequestBody CreateAccountRequest request) {

        return accountService.createAccount(request);
    }

    // 6. Update user
    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật user")
    public ApiResponse<Object> patchUser(
            @PathVariable Long id,
            @RequestBody PatchAccountRequest request) {

        return accountService.patchAccount(id, request);
    }

    // 7. Soft delete -> deactivate
    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá mềm user")
    public ApiResponse<Object> softDeleteUser(@PathVariable Long id) {

        return accountService.deactivateAccount(id);
    }

    // 8. Restore user -> activate
    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục user")
    public ApiResponse<Object> restoreUser(@PathVariable Long id) {

        return accountService.activateAccount(id);
    }
}