package org.example.techstore.repository;

import org.example.techstore.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {
    @Query(value = "SELECT * FROM brands", nativeQuery = true)
    List<Brand> findAllIncludingDeleted();
    @Query(value = "SELECT b FROM Brand b WHERE b.isActive = 1")
    Optional<Brand> findById(Long id);

    boolean existsByBrandCode(String brandCode);
    // Tìm theo id (kể cả đã xoá)
    @Query(value = "SELECT * FROM brands WHERE id = :id", nativeQuery = true)
    Optional<Brand> findByIdIncludingDeleted(Long id);

    Optional<Brand> findByNameIgnoreCase(String name);
}
