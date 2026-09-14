package com.springmatter.relearnspringboot.service;

import com.springmatter.relearnspringboot.dto.record.PatientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {

    Page<PatientResponse> getAll(Pageable pageable);
}
