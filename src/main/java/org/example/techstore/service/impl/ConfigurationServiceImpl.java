package org.example.techstore.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.request.configcuration.ConfigurationRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.entity.Configuration;
import org.example.techstore.exception.AppException;
import org.example.techstore.exception.StatusCode;
import org.example.techstore.repository.ConfigurationRepository;
import org.example.techstore.service.ConfigurationService;
import org.example.techstore.utils.Constant;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigurationServiceImpl implements ConfigurationService {

    ConfigurationRepository configurationRepository;

    @Override
    public ApiResponse<Object> getAll() {
        return ApiResponse.builder()
                .message("Lấy toàn bộ danh sách thành công")
                .result(configurationRepository.findAll())
                .build();
    }

    @Override
    public ApiResponse<Object> getAllActive() {
        return ApiResponse.builder()
                .message("Lấy danh sách đang hoạt động thành công")
                .result(configurationRepository.findByIsActiveTrue())
                .build();
    }

    @Override
    public ApiResponse<Object> getDeleted() {
        return ApiResponse.builder()
                .message("Lấy danh sách đã vô hiệu hóa thành công")
                .result(configurationRepository.findByIsActiveFalse())
                .build();
    }

    @Override
    public ApiResponse<Object> getAnyById(Long id) {
        Configuration config = configurationRepository.findById(id)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST, "Không tìm thấy cấu hình"));
        return ApiResponse.builder()
                .message("Lấy thông tin thành công")
                .result(config)
                .build();
    }

    @Override
    public ApiResponse<Object> getById(Long id) {
        Configuration config = configurationRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST, "Cấu hình không tồn tại hoặc đã bị vô hiệu hóa"));
        return ApiResponse.builder()
                .message("Lấy thông tin thành công")
                .result(config)
                .build();
    }

    @Override
    public ApiResponse<Object> create(ConfigurationRequest request) {
        Configuration config = new Configuration();
        config.setName(request.getName());
        config.setDescription(request.getDescription());

        // AutoCodeListener sẽ tự sinh configurationCode
        configurationRepository.save(config);

        return ApiResponse.builder()
                .message("Tạo cấu hình thành công")
                .result(config)
                .build();
    }

    @Override
    public ApiResponse<Object> update(Long id, ConfigurationRequest request) {
        Configuration config = configurationRepository.findById(id)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST, "Không tìm thấy cấu hình để cập nhật"));

        Optional.ofNullable(request.getName()).ifPresent(config::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(config::setDescription);

        configurationRepository.save(config);

        return ApiResponse.builder()
                .message("Cập nhật thành công")
                .result(config)
                .build();
    }

    @Override
    public ApiResponse<Object> deactivate(Long id) {
        Configuration config = configurationRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST, "Cấu hình không tồn tại hoặc đã bị vô hiệu hóa"));

        config.setIsActive(Constant.IS_ACTIVE.INACTIVE);
        configurationRepository.save(config);

        return ApiResponse.builder()
                .message("Vô hiệu hóa thành công")
                .build();
    }

    @Override
    public ApiResponse<Object> restore(Long id) {
        Configuration config = configurationRepository.findById(id)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST, "Cấu hình không tồn tại"));

        config.setIsActive(Constant.IS_ACTIVE.ACTIVE);
        configurationRepository.save(config);

        return ApiResponse.builder()
                .message("Khôi phục thành công")
                .result(config)
                .build();
    }
}