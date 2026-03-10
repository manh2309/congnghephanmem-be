package org.example.techstore.repository;

import org.example.techstore.entity.Brand;
import org.example.techstore.entity.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecificationRepository extends JpaRepository<Specification, Long> , JpaSpecificationExecutor<Specification> {

    @Query(value = "SELECT * FROM specifications", nativeQuery = true)
    List<Specification> findAllIncludingDeleted();

    @Query("SELECT s FROM Specification s WHERE s.id = :id AND s.isActive = 1")
    Optional<Specification> findActiveById(@Param("id") Long id);

    boolean existsByName(String name);

    @Query(value = "SELECT * FROM specifications WHERE id = :id", nativeQuery = true)
    Optional<Specification> findByIdIncludingDeleted(@Param("id") Long id);

    Optional<Specification> findByNameIgnoreCase(String name);

}
