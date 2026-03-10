package org.example.techstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.techstore.dto.request.categories.CategoryRequest;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.entity.Category;
import org.example.techstore.exception.AppException;
import org.example.techstore.exception.StatusCode;
import org.example.techstore.repository.CategoryRepository;
import org.example.techstore.service.CategoryService;
import org.example.techstore.utils.Constant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public ApiResponse<Object> getAllActive() {

        List<Category> categories = categoryRepository.findAllActive();

        return ApiResponse.builder()
                .message("Lấy danh sách category đang hoạt động thành công")
                .result(categories)
                .build();
    }

    @Override
    public ApiResponse<Object> getAll() {

        List<Category> categories = categoryRepository.findAll();

        return ApiResponse.builder()
                .message("Lấy toàn bộ category thành công")
                .result(categories)
                .build();
    }

    @Override
    public ApiResponse<Object> getById(Long id) {

        Category category = categoryRepository.findActiveById(id)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST, "Không tìm thấy category hoặc đã bị vô hiệu hóa"));

        return ApiResponse.builder()
                .message("Lấy category thành công")
                .result(category)
                .build();
    }

    @Override
    public ApiResponse<Object> getAnyById(Long id) {

        Category category = categoryRepository.findById(id).orElse(null);

        if (category == null) {
            return ApiResponse.builder()
                    .message("Không tìm thấy category")
                    .build();
        }

        return ApiResponse.builder()
                .message("Lấy category thành công")
                .result(category)
                .build();
    }

    @Override
    public ApiResponse<Object> create(CategoryRequest request) {

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        categoryRepository.save(category);

        return ApiResponse.builder()
                .message("Tạo category thành công")
                .result(category)
                .build();
    }

    @Override
    public ApiResponse<Object> update(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id).orElse(null);

        if (category == null) {
            return ApiResponse.builder()
                    .message("Category không tồn tại")
                    .build();
        }

        if (request.getName() != null) {
            category.setName(request.getName());
        }

        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }

        categoryRepository.save(category);

        return ApiResponse.builder()
                .message("Cập nhật category thành công")
                .result(category)
                .build();
    }

    @Override
    public ApiResponse<Object> deactivate(Long id) {

        Category category = categoryRepository.findActiveById(id)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST, "Category không tồn tại hoặc đã bị vô hiệu hóa"));

        category.setIsActive(Constant.IS_ACTIVE.INACTIVE);

        categoryRepository.save(category);

        return ApiResponse.builder()
                .message("Xóa mềm category thành công")
                .build();
    }

    @Override
    public ApiResponse<Object> restore(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(StatusCode.BAD_REQUEST, "Category không tồn tại"));

        category.setIsActive(Constant.IS_ACTIVE.ACTIVE);
        categoryRepository.save(category);

        return ApiResponse.builder()
                .message("Khôi phục category thành công")
                .build();
    }
}
