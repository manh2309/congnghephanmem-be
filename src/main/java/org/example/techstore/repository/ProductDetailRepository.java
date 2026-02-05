package org.example.techstore.repository;

import jakarta.transaction.Transactional;
import org.example.techstore.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
//    @Query("SELECT pd FROM ProductDetail pd WHERE pd.deletedAt IS NULL")
//    List<ProductDetail> findAllNotDeleted();

    @Query("SELECT pd FROM ProductDetail pd")
    List<ProductDetail> findAllIncludingDeleted();

//    @Query("SELECT pd FROM ProductDetail pd WHERE pd.id = :id AND pd.deletedAt IS NULL")
//    ProductDetail findNotDeletedById(@Param("id") Long id);

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.id = :id")
    ProductDetail findAnyById(@Param("id") Long id);

    // Phải trả về Optional
    Optional<ProductDetail> findByProduct_IdAndConfiguration_Id(Long productId, Long configurationId);

    void deleteAllByProductId(Long productId);

    // 🆕 Thêm hàm này để lấy danh sách ProductDetail theo productId
//    @Query("SELECT pd FROM ProductDetail pd " +
//            "LEFT JOIN FETCH pd.configuration c " +
//            "LEFT JOIN FETCH c.specifications s " +
//            "WHERE pd.product.id = :productId AND pd.deletedAt IS NULL")
//    List<ProductDetail> findByProductId(@Param("productId") Long productId);

    // phương thức filter theo category, brand, giá
//    @Query("SELECT pd FROM ProductDetail pd " +
//            "JOIN pd.product p " +
//            "LEFT JOIN p.brand b " +
//            "LEFT JOIN p.category c " +
//            "WHERE pd.deletedAt IS NULL " +
//            "AND (:categoryId IS NULL OR c.id = :categoryId) " +
//            "AND (:brandId IS NULL OR b.id IN :brandId) " +  // <── DÙNG IN
//            "AND (:minPrice IS NULL OR pd.price >= :minPrice) " +
//            "AND (:maxPrice IS NULL OR pd.price <= :maxPrice) " +
//            "ORDER BY " +
//            "CASE WHEN :sortBy = 'price' AND :sortDir = 'asc' THEN pd.price END ASC, " +
//            "CASE WHEN :sortBy = 'price' AND :sortDir = 'desc' THEN pd.price END DESC")
//    List<ProductDetail> findFiltered(
//            @Param("categoryId") Long categoryId,
//            @Param("brandId") List<Long> brandId,
//            @Param("minPrice") Integer minPrice,
//            @Param("maxPrice") Integer maxPrice,
//            @Param("sortBy") String sortBy,
//            @Param("sortDir") String sortDir
//    );

}
