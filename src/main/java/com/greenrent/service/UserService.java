package com.greenrent.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.greenrent.dto.request.AdminUserUpdateRequest;
import com.greenrent.dto.request.UpdatePasswordRequest;
import com.greenrent.dto.request.UserUpdateRequest;
import com.greenrent.exception.BadRequestException;
import com.greenrent.repository.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
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

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private ReservationRepository reservationRepository;

    private UserMapper userMapper;


    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST, registerRequest.getEmail()));
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());


        Role role = roleRepository.findByName(RoleType.ROLE_CUSTOMER).
                orElseThrow(() -> new ResourceNotFoundException
                        (String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, RoleType.ROLE_CUSTOMER.name())));

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
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

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.map(users);
    }


    public Page<UserDTO> getUserPage(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserDTO> dtoPage = users.map(new Function<User, UserDTO>() {

            @Override
            public UserDTO apply(User user) {
                return userMapper.userToUserDTO(user);
            }
        });

        return dtoPage;
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        return userMapper.userToUserDTO(user);
    }

    // Edit Possword
    public void updatePassword(Long id, UpdatePasswordRequest passwordRequest) {
        Optional<User> userOpt = userRepository.findById(id);
        User user = userOpt.get();


        if (user.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }


        //       if(!BCrypt.hashpw(passwordRequest.getOldPassword(), user.getPassword()).equals(user.getPassword())) {
        //           throw new BadRequestException(ErrorMessage.PASSWORD_NOT_MATCHED);
        //       }

        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorMessage.PASSWORD_NOT_MATCHED);
        }

        String hashedPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);

    }


    // Transactional annotasyonu bu method icin aicilir, method bitince kapanir.
    // JPA ile calisirken bu annotasyon ile transaction.start, tx commit, tx.stop islemlerini yapiyor.
    @Transactional
    public void updateUser(Long id, UserUpdateRequest userUpdateRequest) {
        boolean emailExist = userRepository.existsByEmail(userUpdateRequest.getEmail());
        User user = userRepository.findById(id).get();

        if (user.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        if (emailExist && !userUpdateRequest.getEmail().equals(user.getEmail())) {
            throw new ConflictException(ErrorMessage.EMAIL_ALREADY_EXIST);
        }

        userRepository.update(id, userUpdateRequest.getFirstName(), userUpdateRequest.getLastName(),
                userUpdateRequest.getPhoneNumber(), userUpdateRequest.getEmail(), userUpdateRequest.getAddress(), userUpdateRequest.getZipCode());

    }

    // http://localhost:8080/user/1/auth
    /*

    {
    "firstName": "John1",
    "lastName": "Coffee1",
    "password": "admin",
    "phoneNumber": "(222) 317-8828",
    "email": "coffee@email.com",
    "address": "LosAngeles,USA",
    "zipCode": "36548",
    "builtIn": true,
    "roles": [
            "Administrator",
            "Customer"
    ]

}

     */
    // update any user with an id by Admin
    public void updateUserAuth(Long id, AdminUserUpdateRequest adminUserUpdateRequest) {
        // check if email exist or not
        boolean emailExist = userRepository.existsByEmail(adminUserUpdateRequest.getEmail());
        // check if user exist ot not
        User user = userRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,id)));
        // check if user builtin or not
        if (user.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // Eger bir kullanici emaili varsa ve bu email bu kullaniciya esit degilse
        // ayni email den bir kere daha olusturuyor demektir
        // boyle olursa exception yap
        if (emailExist && !adminUserUpdateRequest.getEmail().equals(user.getEmail())) {
            throw new ConflictException(ErrorMessage.EMAIL_ALREADY_EXIST);
        }


        // eger gelen request password bos olabilir, o zaman var olan passwordu alip set edelim
        if (adminUserUpdateRequest.getPassword() == null) {
            adminUserUpdateRequest.setPassword(user.getPassword());
        } else { // eger password geldi ise String formatinda al encode et ve set et
            String encodedPassword = passwordEncoder.encode(adminUserUpdateRequest.getPassword());
            adminUserUpdateRequest.setPassword(encodedPassword);
        }



        // rol bilgisini alan
        Set<String> userStrRoles = adminUserUpdateRequest.getRoles();
        Set<Role> roles = convertRoles(userStrRoles);

       User updateUser=  userMapper.AdminUserUpdateRequestToUser(adminUserUpdateRequest);
       updateUser.setId(user.getId());
       updateUser.setRoles(roles);

       userRepository.save(updateUser);


    }


    // Delete user
    // @Transactional
    public void removeById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new
                ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));

        boolean exists = reservationRepository.existsByUserId(user);
        if(exists) {
            throw new BadRequestException(ErrorMessage.USER_USED_BY_RESERVATION_MESSAGE);
        }

        if (user.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        userRepository.deleteById(id);

       // throw new BadRequestException("occured exception");
        // method basinda @Transactional varsa method icinde ne yaparsa yapsin,
        // exception firlatinca exceptiondan once ne degisiklik yapilmissa geri alir
        // rollback


    }

    public Set<Role> convertRoles(Set<String> pRoles) {
        Set<Role> roles = new HashSet<>();

        // eger role string array i boşsa Customer olarak ekle
        if (pRoles == null) {
            Role userRole = roleRepository.findByName(RoleType.ROLE_CUSTOMER)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, RoleType.ROLE_CUSTOMER.name())));
            roles.add(userRole);
        } else {
            // eger role bos degilse, adminse admin ekle,
            pRoles.forEach(role -> {
                switch (role) {
                    case "Administrator":
                        Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
                                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, RoleType.ROLE_CUSTOMER.name())));
                        roles.add(adminRole);
                        break;

                        // eger role bos degil ve admin degilse customer olarak ekle
                    default:
                        Role userRole = roleRepository.findByName(RoleType.ROLE_CUSTOMER)
                                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, RoleType.ROLE_CUSTOMER.name())));
                        roles.add(userRole);
                }
            });
        }

        return roles;

    }


}