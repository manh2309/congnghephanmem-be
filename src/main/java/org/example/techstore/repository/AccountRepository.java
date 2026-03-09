package org.example.techstore.repository;

import org.example.techstore.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByUsername(String username);

    // Lấy tất cả user kể cả đã xoá
    @Query(value = "SELECT * FROM users", nativeQuery = true)
    List<Account> findAllIncludingDeleted();

    // Lấy user theo id kể cả đã xoá
    @Query(value = "SELECT * FROM users WHERE id = ?1", nativeQuery = true)
    Optional<Account> findByIdIncludingDeleted(Long id);

//    Optional<Account> findByUsernameAndDeletedAtIsNull(String username);

    List<Account> findByRole_Id(Long roleId);

    List<Account> findByIsActiveTrue();
}
