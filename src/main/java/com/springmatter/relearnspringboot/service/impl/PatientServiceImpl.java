package com.springmatter.relearnspringboot.service.impl;

import com.springmatter.relearnspringboot.dto.record.PatientResponse;
import com.springmatter.relearnspringboot.entity.Checkup;
import com.springmatter.relearnspringboot.entity.Patient;
import com.springmatter.relearnspringboot.mapper.PatientMapper;
import com.springmatter.relearnspringboot.repository.PatientRepository;
import com.springmatter.relearnspringboot.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public Page<PatientResponse> getAll(Pageable pageable) {
        Page<Patient> patients = patientRepository.findAll(pageable);
//        if(patients.hasContent()){
//            patientRepository.findAllWithCheckups(patients.getContent());
//        }
        return patients.map(item -> {
            List<String> diagnosis = item.getCheckups().stream()
                    .map(Checkup::getDiagnosis)
                    .toList();
            return patientMapper.mapToPatientResponse(diagnosis, item);
        });
    }
}
