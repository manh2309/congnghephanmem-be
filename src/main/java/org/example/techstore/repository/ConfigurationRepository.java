package org.example.techstore.repository;

import org.example.techstore.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT b FROM Configuration b WHERE b.id = :id AND b.isActive = 1")
    Optional<Configuration> findByIdAndIsActiveTrue(@Param("id") Long id);

    // Dùng để check trùng tên khi tạo mới
    Optional<Configuration> findByNameIgnoreCase(String name);

    // Check trùng tên và còn đang hoạt động
    boolean existsByNameIgnoreCaseAndIsActiveTrue(String name);
}
