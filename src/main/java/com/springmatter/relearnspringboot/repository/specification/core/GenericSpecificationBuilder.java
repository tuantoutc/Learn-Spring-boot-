package com.springmatter.relearnspringboot.repository.specification.core;

import com.springmatter.relearnspringboot.enums.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GenericSpecificationBuilder<T> {

    private final List<SearchCriteria> params;

    public GenericSpecificationBuilder() {
        params = new ArrayList<>();
    }

    // Ghép điều kiện bằng AND (Mặc định isOrPredicate = false)
    public GenericSpecificationBuilder<T> with(String key, SearchOperation operation, Object value) {
        if (value != null) { // Bỏ qua nếu giá trị null
            params.add(new SearchCriteria(key, operation, value, false));
        }
        return this;
    }

    // THÊM MỚI: Ghép điều kiện bằng OR (isOrPredicate = true)
    public GenericSpecificationBuilder<T> withOr(String key, SearchOperation operation, Object value) {
        if (value != null) {
            params.add(new SearchCriteria(key, operation, value, true));
        }
        return this;
    }

    // Phương thức ghép các viên gạch lại bằng toán tử AND
    public Specification<T> build() {
        if (params.isEmpty()) {
            return null;
        }
        // Viên gạch đầu tiên luôn là điểm bắt đầu
        Specification<T> result = new GenericSpecification<T>(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            SearchCriteria criteria = params.get(i);

            // KIỂM TRA: Nếu là gạch OR thì dùng Specification.where().or(...)
            if (criteria.isOrPredicate()) {
                result = Specification.where(result).or(new GenericSpecification<T>(criteria));
            }
            // Ngược lại thì dùng Specification.where().and(...)
            else {
                result = Specification.where(result).and(new GenericSpecification<T>(criteria));
            }
        }
        return result;
    }
}