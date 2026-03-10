package org.example.techstore.repository;

import org.example.techstore.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    // Tìm tất cả theo trạng thái active (true/false)
    @Query(value = "SELECT b FROM Configuration b WHERE b.isActive = 1")
    List<Configuration> findByIsActiveTrue();

    @Query(value = "SELECT b FROM Configuration b WHERE b.isActive = 0")
    List<Configuration> findByIsActiveFalse();

    // Tìm 1 bản ghi đang active
    Optional<Configuration> findByIdAndIsActiveTrue(Long id);

    // Dùng để check trùng tên khi tạo mới
    Optional<Configuration> findByNameIgnoreCase(String name);

    // Check trùng tên và còn đang hoạt động
    boolean existsByNameIgnoreCaseAndIsActiveTrue(String name);
}
