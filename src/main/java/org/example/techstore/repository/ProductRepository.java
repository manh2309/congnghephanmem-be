package org.example.techstore.repository;

import org.example.techstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    List<Product> findAllByIsDeleted(boolean isDeleted);
//
//    Optional<Product> findByIdAndIsDeleted(Long id, boolean isDeleted);

}
