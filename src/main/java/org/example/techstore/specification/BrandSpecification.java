package org.example.techstore.specification;

import org.example.techstore.entity.Brand;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

public class BrandSpecification {

    public static Specification<Brand> searchByKey(String searchKey) {
        return (root, query, cb) -> {
            // Luôn luôn phải có điều kiện này
            Predicate isActive = cb.equal(root.get("isActive"), 1L);

            // Nếu không gõ chữ gì để tìm kiếm, THÌ CHỈ return thằng isActive
            if (searchKey == null || searchKey.trim().isEmpty()) {
                return isActive;
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

            // Gộp cả 2: Phải đang active VÀ (tên giống HOẶC mã giống)
            return cb.and(
                    isActive,
                    cb.or(byCode, byName)
            );
        };
    }
}
