package com.springmatter.relearnspringboot.controller.rest;


import com.springmatter.relearnspringboot.common.ApiResponse;
import com.springmatter.relearnspringboot.dto.record.PatientResponse;
import com.springmatter.relearnspringboot.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/checkups")
    public ApiResponse<Page<PatientResponse>> getAllChekups(@ParameterObject Pageable pageable) {

        return ApiResponse.success(patientService.getAll(pageable));

    }


}
