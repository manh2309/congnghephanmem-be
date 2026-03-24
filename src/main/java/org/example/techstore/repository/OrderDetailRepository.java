package org.example.techstore.repository;

import org.example.techstore.entity.OrderDetail;
import org.example.techstore.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT od FROM OrderDetail od WHERE od.isActive = 1")
    List<OrderDetail> findAllNotDeleted();

    @Query("SELECT od FROM OrderDetail od")
    List<OrderDetail> findAllIncludingDeleted();

    @Query("SELECT od FROM OrderDetail od WHERE od.id = :id AND od.isActive = 1")
    OrderDetail findNotDeletedById(Long id);

    @Query("SELECT od FROM OrderDetail od WHERE od.id = :id")
    OrderDetail findAnyById(Long id);

    Optional<OrderDetail> findByProductDetail_Product_IdAndProductDetail_Configuration_Id(
            Long productId,
            Long configurationId
    );

    @Query("SELECT p.name, SUM(od.quantity) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "JOIN od.productDetail pd " +
            "JOIN pd.product p " +
            "WHERE o.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.name")
    List<Object[]> getSalesByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od " +
            "JOIN od.order o " +
            "JOIN od.productDetail pd " +
            "WHERE pd.product.id = :productId " +
            "AND MONTH(o.createdDate) = :month " +
            "AND YEAR(o.createdDate) = :year " +
            "AND o.status = :status")
    Integer calculateActualSoldPerMonth(
            @Param("productId") Long productId,
            @Param("month") int month,
            @Param("year") int year,
            @Param("status") OrderStatus status);
}
