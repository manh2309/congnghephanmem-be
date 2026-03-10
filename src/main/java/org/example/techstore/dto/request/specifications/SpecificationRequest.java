package org.example.techstore.dto.request.specifications;

import lombok.*;
import org.example.techstore.entity.Configuration;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecificationRequest {
    private String specificationCode;
    private String name;
    private String value;
    private Long configurationId;
}
