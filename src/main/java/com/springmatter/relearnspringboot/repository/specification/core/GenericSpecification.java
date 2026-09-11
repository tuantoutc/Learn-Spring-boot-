package com.springmatter.relearnspringboot.repository.specification.core;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.Collection;


@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T>, Serializable {

    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case EQUAL:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case EQUAL_IGNORE_CASE:
                return builder.equal(
                        builder.lower(root.get(criteria.getKey())),
                        criteria.getValue().toString().toLowerCase()
                );
            case LIKE:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            case LIKE_IGNORE_CASE:
                return builder.like(
                        builder.lower(root.get(criteria.getKey())),
                        "%" + criteria.getValue().toString().toLowerCase() + "%"
                );
            case GREATER_THAN_EQUAL:
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN_EQUAL:
                return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            case GREATER_THAN:
                return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case IN:
                // 1. Khởi tạo biểu thức IN cho cột (field) cần tìm kiếm
                CriteriaBuilder.In<Object> inClause = builder.in(root.get(criteria.getKey()));

                // 2. Kiểm tra xem giá trị truyền vào có phải là 1 danh sách (List/Set) không
                if (criteria.getValue() instanceof Collection<?>) {
                    // Nếu là danh sách, duyệt qua từng phần tử và nạp vào inClause
                    for (Object val : (Collection<?>) criteria.getValue()) {
                        inClause.value(val);
                    }
                } else {
                    // Nếu người dùng chỉ truyền vào 1 giá trị đơn lẻ, nạp luôn giá trị đó
                    inClause.value(criteria.getValue());
                }
                return inClause;
            default:
                return null;
        }
    }
}