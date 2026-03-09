package org.example.techstore.service;

import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.entity.Image;
import org.jspecify.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    ApiResponse<Object> uploadImages(List<MultipartFile> files, Long productId);

    ApiResponse<Object> getAllImages();

    ApiResponse<Object> getImagesByProductId(Long productId);

    ApiResponse<Object> deleteImage(Long id);
}
