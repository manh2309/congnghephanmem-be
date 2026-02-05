package org.example.techstore.repository;

import org.example.techstore.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Lấy tất cả (kể cả brand đã xoá)
    @Query(value = "SELECT * FROM roles", nativeQuery = true)
    List<Role> findAllIncludingDeleted();

    // Tìm theo id (kể cả đã xoá)
    @Query(value = "SELECT * FROM roles WHERE id = :id", nativeQuery = true)
    Optional<Role> findByIdIncludingDeleted(Long id);
}
