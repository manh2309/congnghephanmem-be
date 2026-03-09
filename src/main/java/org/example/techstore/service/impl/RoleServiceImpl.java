package org.example.techstore.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.role.RoleCreateRequest;
import org.example.techstore.dto.request.role.RoleUpdateRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.dto.response.RoleResponse;
import org.example.techstore.entity.Role;
import org.example.techstore.exception.AppException;
import org.example.techstore.exception.StatusCode;
import org.example.techstore.repository.RoleRepository;
import org.example.techstore.service.RoleService;
import org.example.techstore.utils.Constant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository ;
    @Override
    public ApiResponse<Object> findAll() {
        List<Role> roles = roleRepository.findAll();

        List<RoleResponse> data = roles.stream().map(role -> {
            RoleResponse response = new RoleResponse();
            response.setId(role.getId());
            response.setRoleCode(role.getRoleCode());
            response.setName(role.getName());
            response.setDescription(role.getDescription());
            return response;
        }).toList();
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.ROLE))
                .result(data)
                .build();
    }

    @Override
    public ApiResponse<Object> findAllIncludingDeleted() {
        List<Role> roles = roleRepository.findAllIncludingDeleted();

        List<RoleResponse> data = roles.stream().map(role -> {
            RoleResponse response = new RoleResponse();
            response.setId(role.getId());
            response.setRoleCode(role.getRoleCode());
            response.setName(role.getName());
            response.setDescription(role.getDescription());
            response.setIsActive(role.getIsActive());
            return response;
        }).toList();
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.ROLE))
                .result(data)
                .build();
    }

    @Override
    public ApiResponse<Object> findById(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.ROLE))));

        RoleResponse data = null;

        if (role != null) {
            data = new RoleResponse();
            data.setId(role.getId());
            data.setRoleCode(role.getRoleCode());
            data.setName(role.getName());
            data.setDescription(role.getDescription());
            data.setIsActive(role.getIsActive());
        }
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.ROLE))
                .result(data)
                .build();
    }
    @Override
    public ApiResponse<Object> save(RoleCreateRequest request) {
        Role role = new Role();

        role.setRoleCode(request.getRoleCode());
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        Role data = roleRepository.save(role);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.ROLE))
                .result(data)
                .build();
    }
    @Override
    public ApiResponse<Object> patch(Long id, RoleUpdateRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.ROLE))));

        if (request.getRoleCode() != null) {
            role.setRoleCode(request.getRoleCode());
        }

        if (request.getName() != null) {
            role.setName(request.getName());
        }

        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }

        Role saved = roleRepository.save(role);

        RoleResponse data = mapToResponse(saved);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.ROLE))
                .result(data)
                .build();
    }

    private RoleResponse mapToResponse(Role role) {
        RoleResponse response = new RoleResponse();

        response.setId(role.getId());
        response.setRoleCode(role.getRoleCode());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setIsActive(role.getIsActive());

        return response;
    }

    @Override
    public ApiResponse<Object> restore(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() ->  new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.ROLE))));

        role.setIsActive(Constant.IS_ACTIVE.ACTIVE);

        Role saved = roleRepository.save(role);

        RoleResponse data = mapToResponse(saved);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.ROLE))
                .result(data)
                .build();
    }
    @Override
    public ApiResponse<Object> softDelete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST.withMessage(String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.ROLE))));

        role.setIsActive(Constant.IS_ACTIVE.INACTIVE);

        Role saved = roleRepository.save(role);

        RoleResponse data = mapToResponse(saved);
        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.DELETE_SUCCESS, Constant.MODULE.ROLE))
                .result(data)
                .build();
    }
}
