package org.example.techstore.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.techstore.entity.Product;
import org.example.techstore.entity.ProductDetail;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

public class ProductDetailSpecification {
    public static Specification<ProductDetail> searchByKey(String searchKey) {
        return (root, query, cb) -> {

            Predicate isActive = cb.equal(root.get("isActive"), 1L);

            if (searchKey == null || searchKey.trim().isEmpty()) {
                return isActive;
            }

            String keyword = "%" + searchKey.trim().toLowerCase() + "%";

            Join<ProductDetail, Product> productJoin = root.join("product");
            Join<ProductDetail, Configuration> configJoin = root.join("configuration");

            Predicate byProductName = cb.like(
                    cb.lower(productJoin.get("name")),
                    keyword
            );

            Predicate byConfiguration = cb.like(
                    cb.lower(configJoin.get("name")),
                    keyword
            );

            return cb.and(
                    isActive,
                    cb.or(byProductName, byConfiguration)
            );
        };
    }
}
