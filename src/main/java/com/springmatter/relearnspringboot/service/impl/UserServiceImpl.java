package com.springmatter.relearnspringboot.service.impl;


import com.springmatter.relearnspringboot.dto.record.UserRequest;
import com.springmatter.relearnspringboot.dto.record.UserResponse;
import com.springmatter.relearnspringboot.entity.user.User;
import com.springmatter.relearnspringboot.mapper.UserMapper;
import com.springmatter.relearnspringboot.repository.UsersRepository;
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
    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    @Override
    public void create(UserRequest request) {
        usersRepository.save(userMapper.mapToUserEntity(request));
    }

    @Override
    public void update(Long id, UserRequest request) {
        User user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        usersRepository.save(userMapper.mapUpdateUserEntity(request, user));
    }

    @Override
    public void delete(Long id) {
        Optional<User> user = usersRepository.findById(id);
        if(user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        usersRepository.deleteById(id);
    }

    @Override
    public UserResponse getUserById(Long id) {
        Optional<User> user = usersRepository.findById(id);
        if(user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        return userMapper.mapToUserResponse(user.get());
    }

    @Override
    public List<UserResponse> getAll() {
        List<User> users = usersRepository.findAll();
        return users.stream().map(userMapper::mapToUserResponse).toList();
    }

}
