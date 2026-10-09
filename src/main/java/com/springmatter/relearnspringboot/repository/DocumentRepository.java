package com.springmatter.relearnspringboot.repository;

import com.springmatter.relearnspringboot.entity.user.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
