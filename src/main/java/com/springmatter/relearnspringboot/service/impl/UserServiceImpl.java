package com.springmatter.relearnspringboot.service.impl;


import com.springmatter.relearnspringboot.dto.record.UserRequest;
import com.springmatter.relearnspringboot.dto.record.UserResponse;
import com.springmatter.relearnspringboot.entity.User;
import com.springmatter.relearnspringboot.mapper.UserMapper;
import com.springmatter.relearnspringboot.repository.UserRepository;
import com.springmatter.relearnspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public void create(UserRequest request) {
        userRepository.save(userMapper.mapToUserEntity(request));
    }

    @Override
    public void update(Long id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.save(userMapper.mapUpdateUserEntity(request, user));
    }

    @Override
    public void delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        return userMapper.mapToUserResponse(user.get());
    }

    @Override
    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::mapToUserResponse).toList();
    }

}
