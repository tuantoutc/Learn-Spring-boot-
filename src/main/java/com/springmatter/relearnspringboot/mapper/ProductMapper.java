package com.springmatter.relearnspringboot.mapper;


import com.springmatter.relearnspringboot.dto.record.ProductResponse;
import com.springmatter.relearnspringboot.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse mapToProductRepository(Product product);
}
