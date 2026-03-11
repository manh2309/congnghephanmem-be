package org.example.techstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryForecastDTO {
    private String productName;
    private int salesMonthNMinus2;
    private int salesMonthNMinus1;
    private int salesMonthN;
    private int currentStock;
    private int predictedDemand;
}
