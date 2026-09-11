package com.springmatter.relearnspringboot.controller.rest;

import com.springmatter.relearnspringboot.common.ApiResponse;
import com.springmatter.relearnspringboot.common.BaseController;
import com.springmatter.relearnspringboot.dto.record.UserRequest;
import com.springmatter.relearnspringboot.dto.record.UserResponse;
import com.springmatter.relearnspringboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value= "/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getListUser() {
        return createSuccessResponse(userService.getAll());
    }

    @PostMapping
    public ApiResponse<String> createUser(@RequestBody @Valid UserRequest request) {
        userService.create(request);
        return createSuccessResponse("User created");
    }

    @PutMapping("/{id}")
    public ApiResponse<String> updateUser(@PathVariable Long id,
                                             @RequestBody @Valid UserRequest request) {
        userService.update(id, request);
        return createSuccessResponse("User updated");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return createSuccessResponse("User deleted");
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return createSuccessResponse(userService.getUserById(id));
    }
}
