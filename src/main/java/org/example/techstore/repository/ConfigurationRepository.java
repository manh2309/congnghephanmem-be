package org.example.techstore.repository;

import org.example.techstore.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    // Lấy tất cả chưa bị xoá
//    List<Configuration> findAllByDeletedAtIsNull();
//
//    // Lấy tất cả đã bị xoá
//    List<Configuration> findAllByDeletedAtIsNotNull();
//
//    // Lấy 1 bản ghi chưa xoá
//    Optional<Configuration> findByIdAndDeletedAtIsNull(Long id);

    Optional<Configuration> findByNameIgnoreCase(String name);
}
