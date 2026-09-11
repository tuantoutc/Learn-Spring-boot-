package com.springmatter.relearnspringboot.repository;

import com.springmatter.relearnspringboot.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> , JpaSpecificationExecutor<Product> {
    @Query(value = "SELECT p FROM Product p WHERE p.status = 'TRUE' and LOWER(p.name) LIKE %:name% ",
            countQuery ="SELECT count(p) FROM Product p WHERE p.status = 'TRUE' and LOWER(p.name) LIKE %:name%" )
    Page<Product> findByNameAndStatus(String name, String status, Pageable pageable);
}
