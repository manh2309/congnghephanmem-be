package org.example.techstore.repository;

import org.example.techstore.entity.InventoryForecastHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryForecastHistoryRepository extends JpaRepository<InventoryForecastHistory, Long> {

    Optional<InventoryForecastHistory> findByProductNameAndForecastMonthAndForecastYear(String productName, int month, int year);
}
