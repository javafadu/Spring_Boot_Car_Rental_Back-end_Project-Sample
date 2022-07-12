package com.greenrent.controller;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.greenrent.dto.request.UpdatePasswordRequest;
import com.greenrent.dto.request.UserUpdateRequest;
import com.greenrent.dto.response.GRResponse;
import com.greenrent.dto.response.ResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    // Sistemdeki herhangi bir kullanici kendi bilgisini getiriyor.
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<UserDTO> getUserById(HttpServletRequest request){
        Long id= (Long) request.getAttribute("id");
        UserDTO userDTO= userService.findById(id);

        return ResponseEntity.ok(userDTO);
    }


    @GetMapping("/auth/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUserByPage(@RequestParam("page") int page,
                                                          @RequestParam("size") int size,
                                                          @RequestParam("sort") String prop,
                                                          @RequestParam("direction") Sort.Direction direction){

        Pageable pageable=PageRequest.of(page, size, Sort.by(direction,prop));
        Page<UserDTO> userDTOPage=userService.getUserPage(pageable);
        return ResponseEntity.ok(userDTOPage);
    }


    // to get any user in the system, Admin is able to use this method
    // http://localhost:8080/user/3/auth
    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserByIdAdmin(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok(user);

    }


    // update password for a logged user
    // http://localhost:8080/user/auth
    /*

    {
    "newPassword":"testup",
    "oldPassword":"test1"
    }
     */
    @PatchMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<GRResponse> updatePassword(HttpServletRequest httpServletRequest, @RequestBody UpdatePasswordRequest passwordRequest) {
        Long id = (Long) httpServletRequest.getAttribute("id");
        userService.updatePassword(id,passwordRequest);

        GRResponse response = new GRResponse();
        response.setMessage(ResponseMessage.PASSWORD_CHANGES_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }

    // update user own information.

    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<GRResponse> updateUser(HttpServletRequest httpServletRequest, @Valid
                                                 @RequestBody UserUpdateRequest updateRequest) {

        Long id = (Long) httpServletRequest.getAttribute("id");
        userService.updateUser(id,updateRequest);

        GRResponse response = new GRResponse();
        response.setMessage(ResponseMessage.UPDATE_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);

    }


    // Delete user by id
    @DeleteMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GRResponse> deleteUser(@PathVariable Long id) {
        userService.removeById(id);

        GRResponse response = new GRResponse();
        response.setMessage(ResponseMessage.DELETE_RESPONSE_MESSAGE);
        response.setSuccess(true);

        // delete response


        return ResponseEntity.ok(response);
    }

}