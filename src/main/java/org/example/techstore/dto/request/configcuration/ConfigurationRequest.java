package org.example.techstore.dto.request.configcuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationRequest {
    private String name;
    private String description;
}