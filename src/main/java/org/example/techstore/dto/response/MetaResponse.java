package org.example.techstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaResponse {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
