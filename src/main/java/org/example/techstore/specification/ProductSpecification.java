package org.example.techstore.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.techstore.entity.Brand;
import org.example.techstore.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> searchByKey(String searchKey) {
        return (root, query, cb) -> {

            Predicate isActive = cb.equal(root.get("isActive"), 1L);

            if (searchKey == null || searchKey.trim().isEmpty()) {
                return isActive;
            }

            String keyword = "%" + searchKey.trim().toLowerCase() + "%";

            Predicate byCode = cb.like(
                    cb.lower(root.get("productCode")),
                    keyword
            );

            Predicate byName = cb.like(
                    cb.lower(root.get("name")),
                    keyword
            );

            return cb.and(
                    isActive,
                    cb.or(byCode, byName)
            );
        };
    }
}
