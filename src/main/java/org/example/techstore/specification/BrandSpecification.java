package org.example.techstore.specification;

import org.example.techstore.entity.Brand;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

public class BrandSpecification {

    public static Specification<Brand> searchByKey(String searchKey) {
        return (root, query, cb) -> {
            Predicate isActive = cb.equal(root.get("isActive"), 1L);

            if (searchKey == null || searchKey.trim().isEmpty()) {
                return cb.conjunction(); // không filter
            }

            String keyword = "%" + searchKey.trim().toLowerCase() + "%";

            Predicate byCode = cb.like(
                    cb.lower(root.get("brandCode")),
                    keyword
            );

            Predicate byName = cb.like(
                    cb.lower(root.get("name")),
                    keyword
            );

            // brandCode OR name
            return cb.and(
                    isActive,
                    cb.or(byCode, byName)
            );
        };
    }
}
