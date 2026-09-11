package com.springmatter.relearnspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO) // toi uu respone chuan hoa json của rest api hien dai
public class RelearnSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RelearnSpringBootApplication.class, args);
    }
}
