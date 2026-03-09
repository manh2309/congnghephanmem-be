package org.example.techstore.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponse {
    private Long id;
    private String roleCode;
    private String name;
    private String description;
    private Long isActive;
}
