package org.example.techstore.repository;

import org.example.techstore.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
