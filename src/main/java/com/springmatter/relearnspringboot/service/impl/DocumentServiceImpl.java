package com.springmatter.relearnspringboot.service.impl;

import com.springmatter.relearnspringboot.common.FileStorageService;
import com.springmatter.relearnspringboot.common.HybridEncryptionService;
import com.springmatter.relearnspringboot.dto.record.response.DocumentDto;
import com.springmatter.relearnspringboot.dto.record.response.DocumentFileDown;
import com.springmatter.relearnspringboot.dto.record.response.EncryptedDataRes;
import com.springmatter.relearnspringboot.entity.user.Document;
import com.springmatter.relearnspringboot.mapper.DocumentMapper;
import com.springmatter.relearnspringboot.repository.DocumentRepository;
import com.springmatter.relearnspringboot.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final HybridEncryptionService encryptionService;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DocumentMapper mapper = DocumentMapper.INSTANCE;


    // Inner record để serialize nội dung cần mã hóa
    private record DocumentContent(String title, String author, String department, String content) {
    }

    @Transactional
    public Document saveDocument(DocumentDto dto) throws Exception {
        // 1. Tạo đối tượng chứa nội dung cần mã hóa
        DocumentContent content = new DocumentContent(
                dto.title(),
                dto.author(),
                dto.department(),
                dto.content()
        );

        // 2. Chuyển thành JSON và mã hóa
        String json = objectMapper.writeValueAsString(content);
        EncryptedDataRes encrypted = encryptionService.encrypt(json.getBytes());

        // 3. Tạo entity từ DTO (dùng MapStruct)
        Document document = mapper.toEntity(dto);
        document.setEncryptedPayload(objectMapper.writeValueAsString(encrypted));

        // 4. Xử lý file đính kèm (nếu có)
        MultipartFile attachment = dto.attachmentFile();
        if (attachment != null && !attachment.isEmpty()) {
            String originalFilename = attachment.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                originalFilename = "attachment_" + System.currentTimeMillis();
            }

            // Tách phần mở rộng (extension) như .png, .pdf, .docx
            String fileExtension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex >= 0) {
                fileExtension = originalFilename.substring(dotIndex);
            }

            // Tạo tên định danh duy nhất trên MinIO (Object Name) kèm đuôi mở rộng
            String objectName = UUID.randomUUID() + fileExtension;

            // Upload file đã mã hóa lên MinIO (truyền byte[] hoặc InputStream)
            fileStorageService.uploadEncryptedFile(objectName, attachment.getBytes());

            // Lưu thông tin file vào entity
            document.setFileReference(objectName);             // Dùng để tìm file trên MinIO
        }

        return documentRepository.save(document);
    }

    @Transactional(readOnly = true)
    public DocumentDto getDocument(Long id) throws Exception {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // 1. Giải mã payload
        EncryptedDataRes encrypted = objectMapper.readValue(
                document.getEncryptedPayload(),
                EncryptedDataRes.class
        );
        byte[] decryptedBytes = encryptionService.decrypt(encrypted);
        String json = new String(decryptedBytes);

        // 2. Chuyển JSON thành DocumentContent (record)
        DocumentContent content = objectMapper.readValue(json, DocumentContent.class);

        // 3. Tạo DTO từ Entity (MapStruct) và bổ sung content đã giải mã
        DocumentDto dto = mapper.toDto(document);
        // Record không có setter, nên ta tạo mới với content được giải mã
        // Cách 1: dùng builder (nếu record có builder) hoặc tạo mới
        // Vì record là immutable, ta sẽ tạo mới
        return new DocumentDto(
                dto.title(),
                dto.author(),
                dto.department(),
                content.content(),
                null // không có file trong response
        );
    }


    @Transactional(readOnly = true)
    @Override
    public DocumentFileDown downloadDocument(Long id) throws Exception {

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        String fileName = document.getFileReference();
        // 1. Gọi service để lấy mảng byte đã giải mã
        byte[] decryptedData = fileStorageService.downloadDecryptedFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(decryptedData);

        // 2. Xử lý tên file khi tải về (tránh lỗi ký tự tiếng Việt hoặc ký tự đặc biệt)
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replace("+", "%20");

        // 3. Trả về ResponseEntity kèm headers chỉ định download
        return new DocumentFileDown(resource, encodedFileName);
    }
}
