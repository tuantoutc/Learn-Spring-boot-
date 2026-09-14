package com.springmatter.relearnspringboot.dto.record;

import java.time.LocalDateTime;
import java.util.List;

public record PatientResponse(
        Long id,
        String fullName,
        String citizenId,
        LocalDateTime createdAt,
        List<String> diagnosis
) {
}
