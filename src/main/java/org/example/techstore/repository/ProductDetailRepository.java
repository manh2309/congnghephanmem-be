package org.example.techstore.repository;

import jakarta.transaction.Transactional;
import org.example.techstore.entity.Product;
import org.example.techstore.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long>, JpaSpecificationExecutor<ProductDetail> {

    @Query(value = "SELECT * FROM product_details", nativeQuery = true)
    List<ProductDetail> findAllIncludingDeleted();

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.isActive = 1")
    Optional<ProductDetail> findActiveById(@Param("id") Long id);

    @Query(value = "SELECT * FROM product_details WHERE id = :id", nativeQuery = true)
    Optional<ProductDetail> findByIdIncludingDeleted(@Param("id") Long id);


    Optional<ProductDetail> findByProductIdAndConfigurationIdAndIsActiveTrue(Long productId, Long configurationId);

    @Query("""
SELECT DISTINCT pd
FROM ProductDetail pd
LEFT JOIN FETCH pd.configuration c
LEFT JOIN FETCH c.specifications
LEFT JOIN FETCH pd.product p
LEFT JOIN FETCH p.brand
LEFT JOIN FETCH p.category
WHERE p.id = :productId AND pd.isActive = 1
""")
    List<ProductDetail> findByProductId(@Param("productId") Long productId);

    List<ProductDetail> findByProductIdAndIsActiveTrue(Long productId);

    boolean existsByConfigurationId(Long configurationId);

    List<ProductDetail> findByIsActiveTrue();

    Page<ProductDetail> findByIsActiveTrue(Pageable pageable);

}
