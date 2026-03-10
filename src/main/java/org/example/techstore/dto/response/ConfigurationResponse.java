package org.example.techstore.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfigurationResponse {
    private Long id;
    private String configurationCode;
    private String name;
    private String description;
}