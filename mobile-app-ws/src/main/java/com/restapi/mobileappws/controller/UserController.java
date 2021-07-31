package com.restapi.mobileappws.controller;


import com.restapi.mobileappws.SharedDto.AddressDto;
import com.restapi.mobileappws.SharedDto.UserDto;
import com.restapi.mobileappws.exceptions.UserServiceException;
import com.restapi.mobileappws.service.AddressService;
import com.restapi.mobileappws.service.UserService;
import com.restapi.mobileappws.ui.ErrorMessages;
import com.restapi.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.restapi.mobileappws.ui.model.response.AddressResponse;
import com.restapi.mobileappws.ui.model.response.OperationStatusModel;
import com.restapi.mobileappws.ui.model.response.RequestOperationStatus;
import com.restapi.mobileappws.ui.model.response.UserResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")//http://localhost:2009/users
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final AddressService addressService;




    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(value = "page", defaultValue = "0") int page
                                                           , @RequestParam(value = "limit", defaultValue = "2") int limit){

        List<UserResponse> response = new ArrayList<>();

        List<UserDto> users =userService.getListOfUsers(page, limit);


        for(UserDto user: users){
            UserResponse userModel = modelMapper.map(user, UserResponse.class);

            response.add(userModel);
        }


        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<UserResponse> getUser(@PathVariable String id){

        UserDto foundUser = userService.getUserById(id);

        UserResponse response = modelMapper.map(foundUser, UserResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }





    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails){

       // UserResponse response = new UserResponse();

       // UserDto userDto = new UserDto();

//       ModelMapper modelMapper = new ModelMapper();

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);




       // BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser  = userService.createUser(userDto);



//        BeanUtils.copyProperties(createdUser, response);
        UserResponse response = modelMapper.map(createdUser, UserResponse.class);



        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<UserResponse> updateUser(@Valid @PathVariable String id, @RequestBody UserDetailsRequestModel requestModel){

//        UserResponse response = new UserResponse();
//        UserDto userDto = new UserDto();

//        BeanUtils.copyProperties(requestModel, userDto);
        UserDto userDto = modelMapper.map(requestModel, UserDto.class);

        UserDto updatedUserDto = userService.updateUser(id, userDto);

//        BeanUtils.copyProperties(updatedUserDto, response);
        UserResponse response = modelMapper.map(updatedUserDto, UserResponse.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){

        userService.deleteUser(id);

        return new ResponseEntity<>(String.format(" Deleted User with the ID: %s ", id), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/addresses" ,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressResponse>> getUserAddresses(@PathVariable String id){

           List<AddressResponse> addressResponseList = new ArrayList<>();

           List<AddressDto> addressDtos = addressService.getUserAddresses(id);

           addressDtos.forEach(
                   (addressDto) ->{
                       AddressResponse addressResponse = modelMapper.map(addressDto, AddressResponse.class);
                       addressResponseList.add(addressResponse);
                   }
           );

            return new ResponseEntity<>(addressResponseList, HttpStatus.OK);
    }


    @GetMapping(path = "/{userId}/addresses/{addressId}",
     produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressResponse> getAddress(@PathVariable String addressId, @PathVariable String userId){

        AddressDto addressByAddressId = addressService.getAddressByAddressId(addressId);

        AddressResponse addressResponse = modelMapper.map(addressByAddressId, AddressResponse.class);
        //needed for Hateoas
        //localhost:2009/users/{userId}
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");

        //localhost:2009/users/{userId}/addresses
        Link addressLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId))
                //.slash(userId)
                //.slash("addresses")
                .withRel("addresses");

        //localhost:2009/users/{userId}/addresses/{addressId}
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddress(addressId,userId))
               // .slash(userId)
               // .slash("addresses")
               // .slash(addressId)
                .withSelfRel();

        addressResponse.add(userLink, addressLink, selfLink);
        return new ResponseEntity<>(addressResponse, HttpStatus.OK);

    }

    @GetMapping(path = "/email-verification", produces = {MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token){
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        Boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;

    }
}
