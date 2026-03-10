package org.example.techstore.repository;

import org.example.techstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Lấy order theo account
    @Query("""
        SELECT o FROM Order o
        WHERE o.account.id = :accountId
        ORDER BY o.createdDate DESC
    """)
    List<Order> findByAccountId(@Param("accountId") Long accountId);


    // Đếm số order của account
    @Query("""
        SELECT COUNT(o)
        FROM Order o
        WHERE o.account.id = :accountId
          AND o.status <> '5'
    """)
    Long countByAccountId(@Param("accountId") Long accountId);


    // Tổng tiền account đã mua
    @Query("""
        SELECT COALESCE(SUM(o.totalPrice),0)
        FROM Order o
        WHERE o.account.id = :accountId
          AND o.status <> '5'
    """)
    Long sumTotalPriceByAccountId(@Param("accountId") Long accountId);

    @Query("""
    SELECT o FROM Order o
    WHERE o.account.id = :accountId
      AND o.isActive = 1
    ORDER BY o.createdDate DESC
""")
    List<Order> findByAccountIdNotDeleted(@Param("accountId") Long userId);
}
