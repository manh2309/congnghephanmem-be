package org.example.techstore.repository;

import org.example.techstore.entity.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecificationRepository extends JpaRepository<Specification, Long> {
    @Query("SELECT specification FROM Specification specification")
    List<Specification> findAllIncludingDeleted();

//    @Query("SELECT specification FROM Specification specification WHERE specification.deletedAt IS NULL")
//    List<Specification> findAllNotDeleted();

    @Query("SELECT specification FROM Specification specification WHERE specification.id = :id")
    Specification findAnyById(Long id);

//    @Query("SELECT specification FROM Specification specification WHERE specification.id = :id AND specification.deletedAt IS NULL")
//    Specification findNotDeletedById(Long id);
//
//    @Query("SELECT s FROM Specification s WHERE s.configuration.id = :configurationId AND s.deletedAt IS NULL")
//    List<Specification> findByConfigurationId(Long configurationId);

    @Query("SELECT s FROM Specification s WHERE s.configuration.id = :configurationId")
    List<Specification> findAllByConfigurationIdIncludingDeleted(Long configurationId);
}
