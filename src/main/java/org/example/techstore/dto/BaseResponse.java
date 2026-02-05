package org.example.techstore.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseResponse {
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
