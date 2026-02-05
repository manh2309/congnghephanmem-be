package org.example.techstore.repository;

import org.example.techstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders", nativeQuery = true)
    List<Order> findAllIncludingDeleted();

    @Query(value = "SELECT * FROM orders WHERE deleted_at IS NULL", nativeQuery = true)
    List<Order> findAllNotDeleted();

    @Query(value = "SELECT * FROM orders WHERE deleted_at IS NOT NULL", nativeQuery = true)
    List<Order> findAllDeleted();

    @Query(value = "SELECT * FROM orders WHERE id = :id", nativeQuery = true)
    Order findAnyById(@Param("id") Long id);

    @Query(value = "SELECT * FROM orders WHERE id = :id AND deleted_at IS NULL", nativeQuery = true)
    Order findNotDeletedById(@Param("id") Long id);

//    List<Order> findAllByDeletedAtIsNull();

//    @Query("""
//    SELECT o FROM Order o
//    WHERE o.account = :userId
//      AND o.deletedAt IS NULL
//    ORDER BY o.createdDate DESC
//""")
//    List<Order> findByUserIdNotDeleted(@Param("userId") Long userId);

    @Query(
            value = """
    SELECT COUNT(*)
    FROM orders o
    WHERE o.user_id = :userId
      AND o.status <> '5'
  """,
            nativeQuery = true
    )
    Long countByUserId(@Param("userId") Long userId);


    @Query(
            value = """
    SELECT COALESCE(SUM(CAST(o.total_price AS BIGINT)), 0)
    FROM orders o
    WHERE o.user_id = :userId
      AND o.status <> '5'
  """,
            nativeQuery = true
    )
    Long sumTotalPriceByUserId(@Param("userId") Long userId);

}
