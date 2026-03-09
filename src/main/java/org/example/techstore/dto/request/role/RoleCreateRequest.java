package org.example.techstore.dto.request.role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleCreateRequest {
    private String roleCode;
    private String name;
    private String description;
}
