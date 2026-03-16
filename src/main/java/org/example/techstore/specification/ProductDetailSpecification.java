package org.example.techstore.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.techstore.entity.Product;
import org.example.techstore.entity.ProductDetail;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailSpecification {
    public static Specification<ProductDetail> filter(
            String searchKey,
            Long categoryId,
            Long brandId
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (searchKey != null && !searchKey.isEmpty()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("product").get("name")),
                                "%" + searchKey.toLowerCase() + "%"
                        )
                );
            }

            if (categoryId != null) {
                predicates.add(
                        cb.equal(root.get("product").get("category").get("id"), categoryId)
                );
            }

            if (brandId != null) {
                predicates.add(
                        cb.equal(root.get("product").get("brand").get("id"), brandId)
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
