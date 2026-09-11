package com.springmatter.relearnspringboot.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @Column(length = 10)
    private String phone;
    @Column(name= "address", length = 255)
    private String address;

}
