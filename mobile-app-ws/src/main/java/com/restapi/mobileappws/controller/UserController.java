package com.restapi.mobileappws.controller;


import com.restapi.mobileappws.SharedDto.UserDto;
import com.restapi.mobileappws.exceptions.UserServiceException;
import com.restapi.mobileappws.service.UserService;
import com.restapi.mobileappws.ui.ErrorMessages;
import com.restapi.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.restapi.mobileappws.ui.model.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails){

        UserResponse response = new UserResponse();

        UserDto userDto = new UserDto();

        if(userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty()){
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser  = userService.createUser(userDto);


        BeanUtils.copyProperties(createdUser, response);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<UserResponse> updateUser(@Valid @PathVariable String id, @RequestBody UserDetailsRequestModel requestModel){

        UserResponse response = new UserResponse();
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(requestModel, userDto);

        UserDto updatedUserDto = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUserDto,response);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){

        userService.deleteUser(id);

        return new ResponseEntity<>(String.format(" Deleted User with the ID: %s ", id), HttpStatus.OK);
    }
}
