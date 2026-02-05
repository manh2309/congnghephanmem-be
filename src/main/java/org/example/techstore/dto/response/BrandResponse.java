package org.example.techstore.dto.response;

import lombok.*;
import org.example.techstore.dto.BaseResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandResponse extends BaseResponse {
    private Long id;
    private String brandCode;
    private String name;
}
