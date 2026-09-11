package com.springmatter.relearnspringboot.repository.specification.core;

import com.springmatter.relearnspringboot.enums.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class SearchCriteria {
    private String key; // Tên trường (ví dụ: "name", "price")
    private SearchOperation operation; // Phép toán (ví dụ: LIKE_IGNORE_CASE)
    private Object value; // Giá trị tìm kiếm (ví dụ: "iphone", 1000)
    private boolean isOrPredicate;
}
