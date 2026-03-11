package org.example.techstore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "inventory_forecast_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InventoryForecastHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "forecast_month")
    private int forecastMonth;

    @Column(name = "forecast_year")
    private int forecastYear;

    @Column(name = "current_stock_at_time")
    private int currentStockAtTime; // Tồn kho của SP này lúc chạy dự báo

    @Column(name = "predicted_demand")
    private int predictedDemand; // Con số mà Toán học (WMA) tính ra

    @Column(name = "ai_advice", columnDefinition = "TEXT")
    private String aiAdvice; // Lời khuyên của con Gemini lúc đó

    @Column(name = "actual_sold", columnDefinition = "INT DEFAULT 0")
    private int actualSold; // Cuối tháng đối chiếu lại xem bán thực tế được bao nhiêu

    // Tính độ lệch: (actualSold - predictedDemand)
    @Column(name = "accuracy_variance")
    private Integer accuracyVariance;
}
