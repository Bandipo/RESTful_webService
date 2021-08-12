package com.restapi.mobileappws.controller;


import com.restapi.mobileappws.SharedDto.AddressDto;
import com.restapi.mobileappws.SharedDto.UserDto;

import com.restapi.mobileappws.service.AddressService;
import com.restapi.mobileappws.service.UserService;

import com.restapi.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.restapi.mobileappws.ui.model.response.AddressResponse;
import com.restapi.mobileappws.ui.model.response.UserResponse;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")//http://localhost:2009/users
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AddressService addressService;


    @GetMapping(path = "/{id}",
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

        if(page>0) page = page - 1;

          List<UserDto> userDtos = userService.getListOfUsers(page, limit);

        List<UserResponse> userResponseList = userDtos.stream().map(
                (userDto) -> new ModelMapper().map(userDto, UserResponse.class)
        ).collect(Collectors.toList());

        return new ResponseEntity<>(userResponseList, HttpStatus.OK);

    }

    //http://localhost:2009/mobile-app-ws/users/userId/addresses
    @GetMapping(path ="/{userId}/addresses" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserAddresses(@PathVariable String userId){

      List< AddressDto> addressDto = addressService.getUserAddresses(userId);

        List<AddressResponse> addressResponse = addressDto.stream().map(
                (address) -> new ModelMapper().map(address, AddressResponse.class)
        ).collect(Collectors.toList());


        //Adding Hateoas Links for each address
        addressResponse.forEach(
                (addressRest)-> {
                    Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                            .getUserAddress(userId, addressRest.getAddressId())).withSelfRel();
                    // then  we add the link to
                    addressRest.add(selfLink);
                }
        );

        //CollectionModel is used when we want to return Links of list
        //http://localhost:2009/users/<userId>
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId).withRel("user");

        //http://localhost:2009/mobile-app-ws/users/userId/addresses
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserAddresses(userId)).withSelfRel();

        return new ResponseEntity<>(CollectionModel.of(addressResponse, userLink, selfLink), HttpStatus.OK);
    }

    //http://localhost:2009/mobile-app-ws/users/userId/addreses/addressId
    @GetMapping(path = "/{userId}/addresses/{addressId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressResponse> getUserAddress(@PathVariable String userId,
                                                     @PathVariable String addressId){

       AddressDto addressDto = addressService.getUserAddress(addressId, userId);

        AddressResponse response = new ModelMapper().map(addressDto, AddressResponse.class);

        //this will inspect the controller class and create the first part of the link

        //http://localhost:2009/users/<userId>
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId).withRel("user");

        //http://localhost:2009/users/<userId>/addresses
        Link addressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId) )
//                .slash(userId)
//                .slash("addresses")
                .withRel("addresses");

        //http://localhost:2009/mobile-app-ws/users/userId/addreses/addressId
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserAddress(userId, addressId))
//                .slash(userId)
//                .slash("addresses")
//                .slash(addressId)
                .withSelfRel();

        response.add(userLink);// we get this method because AddressResponse extended Repretational Model from Hateos
        response.add(addressesLink);
        response.add(selfLink);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
