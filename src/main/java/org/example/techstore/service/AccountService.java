package org.example.techstore.service;

import org.example.techstore.dto.request.account.CreateAccountRequest;
import org.example.techstore.dto.request.account.PatchAccountRequest;
import org.example.techstore.dto.response.ApiResponse;

public interface AccountService {

    ApiResponse<Object> getAllAccounts();

    ApiResponse<Object> getAllAccountsIncludingInactive();

    ApiResponse<Object> getAccountById(Long id);

    ApiResponse<Object> getUsersRole2Statistics();

    ApiResponse<Object> createAccount(CreateAccountRequest request);

    ApiResponse<Object> patchAccount(Long id, PatchAccountRequest request);

    ApiResponse<Object> deactivateAccount(Long id);

    ApiResponse<Object> activateAccount(Long id);

    ApiResponse<Object> findByEmail(String email);

}
