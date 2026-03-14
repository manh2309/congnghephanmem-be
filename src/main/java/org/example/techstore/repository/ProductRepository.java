package org.example.techstore.repository;

import org.example.techstore.entity.Brand;
import org.example.techstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

        @Query(value = "SELECT * FROM products", nativeQuery = true)
        List<Product> findAllIncludingDeleted();

        @Query("SELECT p FROM Product p WHERE p.isActive = 1")

        Optional<Product> findActiveById(Long id);

        boolean existsByName(String productName);

        @Query(value = "SELECT * FROM products WHERE id = :id", nativeQuery = true)
        Optional<Product> findByIdIncludingDeleted(Long id);

        Optional<Product> findByNameIgnoreCase(String name);

}
