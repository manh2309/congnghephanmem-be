package org.example.techstore.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {
    private Long id;

    private String imageUrl;

    private String publicId;

    private Boolean isMain;

    private Long productId;

}
