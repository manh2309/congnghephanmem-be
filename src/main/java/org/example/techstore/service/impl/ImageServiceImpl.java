package org.example.techstore.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.techstore.dto.response.ApiResponse;
import org.example.techstore.dto.response.ImageResponse;
import org.example.techstore.entity.Image;
import org.example.techstore.entity.Product;
import org.example.techstore.exception.AppException;
import org.example.techstore.exception.StatusCode;
import org.example.techstore.repository.ImageRepository;
import org.example.techstore.repository.ProductRepository;
import org.example.techstore.service.ImageService;
import org.example.techstore.utils.Constant;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageServiceImpl implements ImageService {

    ImageRepository imageRepository;
    ProductRepository productRepository;
    Cloudinary cloudinary;

    @Override
    public ApiResponse<Object> uploadImages(List<MultipartFile> files, Long productId) {

        if (files == null || files.isEmpty()) {
            throw new AppException(
                    StatusCode.BAD_REQUEST.withMessage("Files cannot be empty")
            );
        }

        Product product = null;

        if (productId != null) {
            product = productRepository.findById(productId)
                    .orElseThrow(() -> new AppException(
                            StatusCode.BAD_REQUEST.withMessage(
                                    String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.PRODUCT)
                            )
                    ));
        }

        List<Image> images = new ArrayList<>();
        List<ImageResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            try {

                Map<String, Object> uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.emptyMap()
                );

                Image image = Image.builder()
                        .imageUrl(uploadResult.get("secure_url").toString())
                        .publicId(uploadResult.get("public_id").toString())
                        .isMain(false)
                        .product(product)
                        .build();

                images.add(image);

            } catch (Exception e) {
                throw new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                Constant.ERROR_MESSAGE.UPLOAD_FILE_FOUND
                        )
                );
            }
        }

        List<Image> savedImages = imageRepository.saveAll(images);

        for (Image saved : savedImages) {
            ImageResponse response = ImageResponse.builder()
                    .id(saved.getId())
                    .imageUrl(saved.getImageUrl())
                    .isMain(saved.getIsMain())
                    .productId(saved.getProduct() != null ? saved.getProduct().getId() : null)
                    .build();

            responses.add(response);
        }

        return ApiResponse.builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.CREATE_SUCCESS, Constant.MODULE.IMAGE))
                .result(responses)
                .build();
    }
    @Override
    public ApiResponse<Object> getAllImages() {

        List<Image> images = imageRepository.findAll();

        List<ImageResponse> responses = images.stream()
                .map(image -> ImageResponse.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .isMain(image.getIsMain())
                        .productId(image.getProduct() != null ? image.getProduct().getId() : null)
                        .productName(image.getProduct() != null ? image.getProduct().getName() : null) // ⭐ thêm dòng này
                        .build())
                .toList();

        return ApiResponse.<Object>builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.IMAGE))
                .result(responses)
                .build();
    }

    @Override
    public ApiResponse<Object> getImagesByProductId(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.PRODUCT)
                        )
                ));

        List<Image> images = imageRepository.findByProduct_Id(productId);

        List<ImageResponse> responses = images.stream()
                .map(image -> ImageResponse.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .isMain(image.getIsMain())
                        .productId(product.getId())
                        .build())
                .toList();

        return ApiResponse.<Object>builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.SEARCH_SUCCESS, Constant.MODULE.IMAGE))
                .result(responses)
                .build();
    }
    @Override
    public ApiResponse<Object> deleteImage(Long id) {

        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new AppException(
                        StatusCode.BAD_REQUEST.withMessage(
                                String.format(Constant.ERROR_MESSAGE.NOT_FOUND, Constant.MODULE.IMAGE)
                        )
                ));

        try {
            cloudinary.uploader().destroy(
                    image.getPublicId(),
                    ObjectUtils.emptyMap()
            );
        } catch (Exception e) {
            throw new RuntimeException("Delete image on Cloudinary failed");
        }

        imageRepository.delete(image);

        return ApiResponse.<Object>builder()
                .code(StatusCode.SUCCESS.getCode())
                .message(String.format(Constant.MESSAGE.DELETE_SUCCESS, Constant.MODULE.IMAGE))
                .result(id)
                .build();
    }
}
