package com.greenrent.dto.mapper;

import java.util.List;

import com.greenrent.dto.request.AdminUserUpdateRequest;
import org.mapstruct.Mapper;

import com.greenrent.domain.User;
import com.greenrent.dto.UserDTO;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // tek user i userDTO ya cevir
    UserDTO userToUserDTO(User user);
    // User listesini UserDTO ya cevir
    List<UserDTO> map(List<User> user);

    @Mapping(target="id", ignore = true)
    @Mapping(target="roles", ignore = true)
    User AdminUserUpdateRequestToUser(AdminUserUpdateRequest request);

}