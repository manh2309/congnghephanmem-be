package org.example.techstore.service;

import org.example.techstore.entity.InventoryForecastHistory;

import java.util.List;
import java.util.Map;

public interface AiAnalyticsService {
    Map<String, Object> generateSmartInventoryReport();
    List<InventoryForecastHistory> getProductForecastHistory(String productName);
}
