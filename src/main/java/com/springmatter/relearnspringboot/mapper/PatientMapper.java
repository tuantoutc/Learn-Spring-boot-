package com.springmatter.relearnspringboot.mapper;


import com.springmatter.relearnspringboot.dto.record.PatientResponse;
import com.springmatter.relearnspringboot.entity.Patient;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientResponse mapToPatientResponse(List<String> diagnosis, Patient patient);
}
