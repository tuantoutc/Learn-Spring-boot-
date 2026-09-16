package com.springmatter.relearnspringboot.repository;


import com.springmatter.relearnspringboot.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    @Query("SELECT c FROM Category c WHERE c IN :categories ")
    @EntityGraph(attributePaths = {"products"})
    List<Category> getCategoriesWithProducts(List<Category> categories);
}
