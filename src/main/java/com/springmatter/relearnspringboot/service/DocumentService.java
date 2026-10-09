package com.springmatter.relearnspringboot.service;

import com.springmatter.relearnspringboot.dto.record.response.DocumentDto;
import com.springmatter.relearnspringboot.entity.user.Document;

public interface DocumentService {

    Document saveDocument(DocumentDto dto) throws Exception;

    DocumentDto getDocument(Long id) throws Exception;
}
