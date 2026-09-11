package com.springmatter.relearnspringboot.mapper;

import com.springmatter.relearnspringboot.dto.record.UserRequest;
import com.springmatter.relearnspringboot.dto.record.UserResponse;
import com.springmatter.relearnspringboot.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserResponse mapToUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    User mapToUserEntity(UserRequest request);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    User mapUpdateUserEntity(UserRequest request, @MappingTarget User user);
}
