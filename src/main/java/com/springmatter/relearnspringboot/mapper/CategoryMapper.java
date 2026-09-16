package com.springmatter.relearnspringboot.mapper;


import com.springmatter.relearnspringboot.dto.record.CategoryRequest;
import com.springmatter.relearnspringboot.dto.record.CategoryResponse;
import com.springmatter.relearnspringboot.dto.record.ProductResponse;
import com.springmatter.relearnspringboot.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toCategoryEnity(CategoryRequest rq);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category updateCategory(CategoryRequest rq, @MappingTarget Category category);


    @Mapping(source = "productResponses", target = "products")
    CategoryResponse toCategoryResponse(Category category, List<ProductResponse> productResponses);
}
