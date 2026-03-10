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
    @Query(value = "SELECT * FROM categories WHERE is_active = 1", nativeQuery = true)
    List<Category> findAllActive();

    @Query(value = "SELECT * FROM categories", nativeQuery = true)
    List<Category> findAllIncludingDeleted();

    @Query(value = "SELECT * FROM categories WHERE id = :id AND is_active = 1", nativeQuery = true)
    Optional<Category> findActiveById(@Param("id") Long id);

    @Query(value = "SELECT * FROM categories WHERE name = :name AND is_active = 1 LIMIT 1", nativeQuery = true)
    Optional<Category> findByNameActive(@Param("name") String name);
}
