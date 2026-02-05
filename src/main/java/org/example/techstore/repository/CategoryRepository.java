package org.example.techstore.repository;

import org.example.techstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Lấy tất cả category chưa bị xoá (deleted_at IS NULL)
    @Query(value = "SELECT * FROM categories WHERE deleted_at IS NULL", nativeQuery = true)
    List<Category> findAllActive();

    // Lấy tất cả category kể cả đã xoá
    @Query(value = "SELECT * FROM categories", nativeQuery = true)
    List<Category> findAllIncludingDeleted();

    // Tìm 1 category theo id (chỉ lấy cái chưa bị xoá)
    @Query(value = "SELECT * FROM categories WHERE id = :id AND deleted_at IS NULL", nativeQuery = true)
    Optional<Category> findActiveById(@Param("id") Long id);

    // Tìm 1 category theo id (kể cả đã xoá)
    @Query(value = "SELECT * FROM categories WHERE id = :id", nativeQuery = true)
    Optional<Category> findByIdIncludingDeleted(@Param("id") Long id);

    @Query(value = "SELECT * FROM categories WHERE name = :name LIMIT 1", nativeQuery = true)
    Optional<Category> findByNameIncludingDeleted(@Param("name") String name);

    // 🔍 Tìm category đang hoạt động theo tên
    @Query(value = "SELECT * FROM categories WHERE name = :name AND deleted_at IS NULL LIMIT 1", nativeQuery = true)
    Optional<Category> findByNameActive(@Param("name") String name);

    Optional<Category> findByNameIgnoreCase(String name);
}
