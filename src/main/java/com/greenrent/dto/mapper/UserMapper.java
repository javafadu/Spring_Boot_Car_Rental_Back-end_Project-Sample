package com.greenrent.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.greenrent.domain.User;
import com.greenrent.dto.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // tek user i userDTO ya cevir
    UserDTO userToUserDTO(User user);
    // User listesini UserDTO ya cevir
    List<UserDTO> map(List<User> user);

}