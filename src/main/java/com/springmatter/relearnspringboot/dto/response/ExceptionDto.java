package com.springmatter.relearnspringboot.dto.response;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDto {
    private Long code;
    private String message;
}
