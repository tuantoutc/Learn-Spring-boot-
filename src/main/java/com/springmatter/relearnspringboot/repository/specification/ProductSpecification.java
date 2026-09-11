package com.springmatter.relearnspringboot.repository.specification;

import com.springmatter.relearnspringboot.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> filterProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, String category, String status) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(name)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (StringUtils.hasText(category)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("category")), "%" + category.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(status)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), status.toLowerCase()));
            }
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));

        });

    }
}
