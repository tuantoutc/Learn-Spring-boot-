package com.springmatter.relearnspringboot.repository;


import com.springmatter.relearnspringboot.entity.Patient;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query(value = "SELECT DISTINCT p FROM Patient p JOIN FETCH p.checkups WHERE p IN :patients")
    List<Patient> findAllWithCheckup(@Param("patients") List<Patient> patients);


    @Query(value = "SELECT p FROM Patient p WHERE p IN :patients")
    @EntityGraph(attributePaths = "checkups")
    List<Patient> findAllWithCheckups(@Param("patients") List<Patient> patients);
}
