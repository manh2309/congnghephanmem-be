package org.example.techstore.dto.request.image;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageUploadRequest {
    private Long productId;
}
