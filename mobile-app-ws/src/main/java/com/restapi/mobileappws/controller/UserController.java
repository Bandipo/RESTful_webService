package com.restapi.mobileappws.controller;


import com.restapi.mobileappws.SharedDto.UserDto;
import com.restapi.mobileappws.service.UserService;
import com.restapi.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.restapi.mobileappws.ui.model.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")//http://localhost:2009/users
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<UserResponse> getUser(@PathVariable String id){
        UserResponse response = new UserResponse();
        UserDto foundUser = userService.getUserById(id);

        BeanUtils.copyProperties(foundUser, response);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }





    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<UserResponse> createUser(@RequestBody UserDetailsRequestModel userDetails){

        UserResponse response = new UserResponse();

        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser  = userService.createUser(userDto);


        BeanUtils.copyProperties(createdUser, response);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public  String updateUser(){
        return "updateUser was called";
    }

    @DeleteMapping
    public String deleteUser(){
        return "deleteUser was called";
    }
}
