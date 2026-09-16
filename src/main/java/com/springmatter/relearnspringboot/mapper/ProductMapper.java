package com.springmatter.relearnspringboot.mapper;


import com.springmatter.relearnspringboot.dto.record.ProductRequest;
import com.springmatter.relearnspringboot.dto.record.ProductResponse;
import com.springmatter.relearnspringboot.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    @Mapping(target = "category", ignore = true)
    ProductResponse mapToProductResponse(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    Product toProductEntity(ProductRequest rq);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product updateProductEntity(ProductRequest rq, @MappingTarget Product product);
}
