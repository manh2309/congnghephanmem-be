package org.example.techstore.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationSpecification {
    public static Specification<org.example.techstore.entity.Specification> searchByKey(String searchKey) {
        return (root, query, cb) -> {

            if (searchKey == null || searchKey.isBlank()) {
                return cb.equal(root.get("isActive"), 1);
            }

            String likeKey = "%" + searchKey.toLowerCase() + "%";

            return cb.and(
                    cb.equal(root.get("isActive"), 1),
                    cb.or(
                            cb.like(cb.lower(root.get("specificationCode")), likeKey),
                            cb.like(cb.lower(root.get("name")), likeKey),
                            cb.like(cb.lower(root.get("value")), likeKey)
                    )
            );
        };
    }
}
