package com.springmatter.relearnspringboot.service;

import com.springmatter.relearnspringboot.dto.record.UserRequest;
import com.springmatter.relearnspringboot.dto.record.UserResponse;

import java.util.List;

public interface UserService {
    void create(UserRequest request);

    void update(Long id, UserRequest request);

    void delete(Long id);

    UserResponse getUserById(Long id);

    List<UserResponse> getAll();


}
