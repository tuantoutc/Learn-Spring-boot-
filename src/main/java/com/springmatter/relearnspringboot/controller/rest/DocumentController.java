package com.springmatter.relearnspringboot.controller.rest;

import com.springmatter.relearnspringboot.dto.record.response.DocumentDto;
import com.springmatter.relearnspringboot.dto.record.response.DocumentFileDown;
import com.springmatter.relearnspringboot.entity.user.Document;
import com.springmatter.relearnspringboot.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> createDocument(@ModelAttribute DocumentDto dto) throws Exception {
        Document saved = documentService.saveDocument(dto);
        return ResponseEntity.ok(Map.of(
                "id", saved.getId(),
                "message", "Tài liệu đã được lưu với mã hóa"
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<?> getDocument(@PathVariable Long id) throws Exception {
        DocumentDto dto = documentService.getDocument(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws Exception {
        DocumentFileDown documentFileDown = documentService.downloadDocument(id);
        MediaType mediaType = MediaTypeFactory.getMediaType(documentFileDown.encodedFileName())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(documentFileDown.resource().contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + documentFileDown.encodedFileName() + "\"; filename*=UTF-8''" + documentFileDown.encodedFileName())
                .body(documentFileDown.resource());
    }


}