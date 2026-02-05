package org.example.techstore.repository;

import org.example.techstore.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
//    @Query("SELECT OrderDetail FROM OrderDetail WHERE deletedAt IS NULL")
//    List<OrderDetail> findAllNotDeleted();
//
//    @Query("SELECT OrderDetail FROM OrderDetail")
//    List<OrderDetail> findAllIncludingDeleted();
//
//    @Query("SELECT OrderDetail FROM OrderDetail WHERE id = :id AND deletedAt IS NULL")
//    OrderDetail findNotDeletedById(Long id);
//
//    @Query("SELECT OrderDetail FROM OrderDetail WHERE id = :id")
//    OrderDetail findAnyById(Long id);
//
//    Optional<OrderDetail> findByProductDetail_Product_IdAndProductDetail_Configuration_Id(Long productId, Long configurationId);

}
