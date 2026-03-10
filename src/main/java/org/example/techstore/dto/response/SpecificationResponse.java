package org.example.techstore.dto.response;

import lombok.*;
import org.example.techstore.entity.Configuration;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationResponse {
    private Long id;
    private String specificationCode;
    private String name;
    private String value;
    private Configuration configuration;
}
