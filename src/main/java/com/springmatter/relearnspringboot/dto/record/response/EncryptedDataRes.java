package com.springmatter.relearnspringboot.dto.record.response;

public record EncryptedDataRes(
    String encryptedKey,
    String iv,
    String encryptedData
) {}