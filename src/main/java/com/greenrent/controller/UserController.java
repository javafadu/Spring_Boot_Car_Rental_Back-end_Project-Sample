package com.greenrent.controller;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.greenrent.dto.UserDTO;
import com.greenrent.service.UserService;
import lombok.AllArgsConstructor;
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {


    private UserService userService;

    @GetMapping("/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> users=userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<UserDTO> getUserById(HttpServletRequest request){
        Long id= (Long) request.getAttribute("id");
        UserDTO userDTO= userService.findById(id);

        return ResponseEntity.ok(userDTO);
    }

}