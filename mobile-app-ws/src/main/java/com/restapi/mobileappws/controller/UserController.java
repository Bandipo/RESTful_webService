package com.restapi.mobileappws.controller;


import com.restapi.mobileappws.SharedDto.UserDto;

import com.restapi.mobileappws.service.UserService;

import com.restapi.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.restapi.mobileappws.ui.model.response.UserResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")//http://localhost:2009/users
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<UserResponse> getUser(@PathVariable String id){

        UserDto foundUser = userService.getUserById(id);

        UserResponse userResponse = new ModelMapper().map(foundUser, UserResponse.class);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }





    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails){


        UserDto userDto = new ModelMapper().map(userDetails, UserDto.class);

        UserDto createdUser  = userService.createUser(userDto);

        UserResponse userResponse = new ModelMapper().map(createdUser, UserResponse.class);


        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }




    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<UserResponse> updateUser(@Valid @PathVariable String id, @RequestBody UserDetailsRequestModel requestModel){

        UserDto userDto = new ModelMapper().map(requestModel, UserDto.class);

        UserDto updatedUserDto = userService.updateUser(id, userDto);

        UserResponse userResponse = new ModelMapper().map(updatedUserDto, UserResponse.class);


        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){

        userService.deleteUser(id);

        return new ResponseEntity<>(String.format(" Deleted User with the ID: %s ", id), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponse>> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "limit", defaultValue = "10") int limit){

          List<UserDto> userDtos = userService.getListOfUsers(page, limit);

        List<UserResponse> userResponseList = userDtos.stream().map(
                (userDto) -> {
                    return new ModelMapper().map(userDto, UserResponse.class);

                }
        ).collect(Collectors.toList());

        return new ResponseEntity<>(userResponseList, HttpStatus.OK);

    }
}
