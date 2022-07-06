package com.greenrent.service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.greenrent.domain.Role;
import com.greenrent.domain.User;
import com.greenrent.domain.enums.RoleType;
import com.greenrent.dto.UserDTO;
import com.greenrent.dto.mapper.UserMapper;
import com.greenrent.dto.request.RegisterRequest;
import com.greenrent.exception.ConflictException;
import com.greenrent.exception.ResourceNotFoundException;
import com.greenrent.exception.message.ErrorMessage;
import com.greenrent.repository.RoleRepository;
import com.greenrent.repository.UserRepository;
import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private UserMapper userMapper;


    public void register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST,registerRequest.getEmail()));
        }

        String encodedPassword= passwordEncoder.encode(registerRequest.getPassword());


        Role role= roleRepository.findByName(RoleType.ROLE_CUSTOMER).
                orElseThrow(()->new ResourceNotFoundException
                        (String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE,RoleType.ROLE_CUSTOMER.name())));

        Set<Role> roles=new HashSet<>();
        roles.add(role);

        User user=new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setAddress(registerRequest.getAddress());
        user.setZipCode(registerRequest.getZipCode());
        user.setRoles(roles);

        userRepository.save(user);
    }

    public List<UserDTO> getAllUsers(){
        List<User> users= userRepository.findAll();
        return userMapper.map(users);
    }


    public UserDTO findById(Long id) {
        User user=userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        return userMapper.userToUserDTO(user);
        // return UserMapper.INSTANCE.userToUserDTO(user);
    }




}