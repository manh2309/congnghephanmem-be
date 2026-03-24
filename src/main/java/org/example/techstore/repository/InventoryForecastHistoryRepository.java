package org.example.techstore.repository;

import org.example.techstore.entity.InventoryForecastHistory;
import org.example.techstore.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryForecastHistoryRepository extends JpaRepository<InventoryForecastHistory, Long> {

    Optional<InventoryForecastHistory> findByProductNameAndForecastMonthAndForecastYear(String productName, int month, int year);

    List<InventoryForecastHistory> findByProductName(String productName);
}
