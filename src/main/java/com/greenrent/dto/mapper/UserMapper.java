package com.greenrent.dto.mapper;
import java.util.List;
import org.mapstruct.Mapper;
import com.greenrent.domain.User;
import com.greenrent.dto.UserDTO;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
    UserDTO userToUserDTO(User user);
    List<UserDTO> map(List<User> user);
}