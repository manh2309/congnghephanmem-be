package org.example.techstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageController {
    ImageService imageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Object> uploadImages(@RequestParam("files") List<MultipartFile> files, @RequestParam(required = false) Long productId) {
        return imageService.uploadImages(files, productId);
    }

    @Operation(summary = "Lấy toàn bộ ảnh (mọi product)")
    @GetMapping
    public ApiResponse<Object> getAllImages() {
        return imageService.getAllImages();
    }

    @Operation(summary = "Lấy tất cả ảnh theo productId")
    @GetMapping("/product/{productId}")
    public ApiResponse<Object> getImagesByProductId(@PathVariable Long productId) {
        return imageService.getImagesByProductId(productId);
    }

    @Operation(summary = "Xóa ảnh")
    @DeleteMapping("/{id}")
    public ApiResponse<Object> deleteImage(@PathVariable Long id) {
        return imageService.deleteImage(id);
    }
}
