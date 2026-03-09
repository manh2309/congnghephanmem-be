package org.example.techstore.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.account.CreateAccountRequest;
import org.example.techstore.dto.request.account.PatchAccountRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.dto.response.account.UserStatisticResponse;
import org.example.techstore.entity.Account;
import org.example.techstore.entity.Role;
import org.example.techstore.repository.AccountRepository;
import org.example.techstore.repository.OrderRepository;
import org.example.techstore.repository.RoleRepository;
import org.example.techstore.service.AccountService;
import org.example.techstore.utils.Constant;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    OrderRepository orderRepository;

    @Override
    public ApiResponse<Object> getAllAccounts() {

        List<Account> accounts = accountRepository.findByIsActiveTrue();

        return ApiResponse.builder()
                .code(200)
                .message("Lấy danh sách account thành công")
                .result(accounts)
                .build();
    }

    @Override
    public ApiResponse<Object> getAllAccountsIncludingInactive() {

        List<Account> accounts = accountRepository.findAll();

        return ApiResponse.builder()
                .code(200)
                .message("Lấy tất cả account")
                .result(accounts)
                .build();
    }

    @Override
    public ApiResponse<Object> getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return ApiResponse.builder()
                .code(200)
                .message("Lấy account thành công")
                .result(account)
                .build();
    }

    public ApiResponse<Object> getUsersRole2Statistics() {

        List<UserStatisticResponse> statistics = accountRepository.findByRole_Id(2L)
                .stream()
                .map(account -> {

                    Long totalOrders = orderRepository.countByAccountId(account.getId());

                    Long totalSpent = Optional
                            .ofNullable(orderRepository.sumTotalPriceByAccountId(account.getId()))
                            .orElse(0L);

                    return new UserStatisticResponse(
                            account.getId(),
                            account.getUsername(),
                            account.getEmail(),
                            account.getPhoneNumber(),
                            account.getAddress(),
                            totalOrders,
                            totalSpent
                    );

                })
                .toList();

        return ApiResponse.builder()
                .code(200)
                .message("Lấy thống kê người dùng thành công")
                .result(statistics)
                .build();
    }

    @Override
    public ApiResponse<Object> createAccount(CreateAccountRequest request) {

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Account account = new Account();

        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setPhoneNumber(request.getPhoneNumber());
        account.setAddress(request.getAddress());
        account.setRole(role);
        accountRepository.save(account);

        return ApiResponse.builder()
                .code(200)
                .message("Tạo account thành công")
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> patchAccount(Long id, PatchAccountRequest request) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (request.getUsername() != null) account.setUsername(request.getUsername());
        if (request.getEmail() != null) account.setEmail(request.getEmail());
        if (request.getPassword() != null)
            account.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getPhoneNumber() != null) account.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null) account.setAddress(request.getAddress());

        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            account.setRole(role);
        }

        accountRepository.save(account);

        return ApiResponse.builder()
                .code(200)
                .message("Cập nhật account thành công")
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> deactivateAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setIsActive(Constant.IS_ACTIVE.INACTIVE);

        accountRepository.save(account);

        return ApiResponse.builder()
                .code(200)
                .message("Deactivate account thành công")
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> activateAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setIsActive(Constant.IS_ACTIVE.ACTIVE);

        accountRepository.save(account);

        return ApiResponse.builder()
                .code(200)
                .message("Activate account thành công")
                .result(null)
                .build();
    }

    @Override
    public ApiResponse<Object> findByEmail(String email) {

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return ApiResponse.builder()
                .code(200)
                .message("Tìm account theo email thành công")
                .result(account)
                .build();
    }
}