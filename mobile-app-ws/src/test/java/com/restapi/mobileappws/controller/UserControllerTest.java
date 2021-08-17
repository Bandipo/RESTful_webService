package com.restapi.mobileappws.controller;

import com.restapi.mobileappws.SharedDto.AddressDto;
import com.restapi.mobileappws.SharedDto.UserDto;

import com.restapi.mobileappws.service.UserService;
import com.restapi.mobileappws.ui.model.request.AddressRequestModel;
import com.restapi.mobileappws.ui.model.request.UserDetailsRequestModel;

import com.restapi.mobileappws.ui.model.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;


    String userId = "djksjkd89382";
    String email = "bandipotaiye@gmail.com";
    UserDto userDto;
    UserResponse userResponse;
    UserDetailsRequestModel userDetails;


    @BeforeEach
    void setUp() {
            //User Dto
        userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setEmail(email);
        userDto.setLastName("Bandipo");
        userDto.setFirstName("Taiye");

        //User Response
        userResponse = new UserResponse();
        userResponse.setUserId(userId);
        userResponse.setEmail(email);
        userResponse.setFirstName("Paul");
        userResponse.setLastName("BAOG");

        //UserDetails Request Model

        userDetails = new UserDetailsRequestModel();
        userDetails.setFirstName("Taiye");
        userDetails.setLastName("Bandipo");
        userDetails.setEmail("email");
        userDetails.setAddresses(getAddressRequestModel());

    }

    @Test
    void getUser() {

        //when
        when(userService.getUserById(anyString())).thenReturn(userDto);

        ResponseEntity<UserResponse> user = userController.getUser(userId);

        assertTrue(user.getStatusCode().is2xxSuccessful());
        assertNotNull(user);
        assertEquals(Objects.requireNonNull(user.getBody()).getUserId(), userDto.getUserId());
        assertEquals(user.getBody().getFirstName(), userDto.getFirstName());
    }


    @Test
    void should_Create_User(){

        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        //Then when we call

        ResponseEntity<UserResponse> user = userController.createUser(userDetails);

        //Then assert

        assertNotNull(user);
        assertTrue(user.getStatusCode().is2xxSuccessful());
        assertEquals(userDto.getUserId(), user.getBody().getUserId() );



    }

    private List<AddressDto> getAddressDto(){
        AddressDto addressDto = new AddressDto();
        addressDto.setType("shipping");
        addressDto.setId(1L);
        addressDto.setCity("Lagos");
        addressDto.setCountry("Nigeria");
        addressDto.setPostalCode("250111");

        AddressDto billingAddressDto = new AddressDto();

        billingAddressDto.setType("shipping");
        billingAddressDto.setId(1L);
        billingAddressDto.setCity("Lagos");
        billingAddressDto.setCountry("Nigeria");
        billingAddressDto.setPostalCode("250111");

        List<AddressDto> addresses = new ArrayList<>();

        addresses.add(addressDto);
        addresses.add(billingAddressDto);
        return addresses;
    }


    private List<AddressRequestModel> getAddressRequestModel(){

        List<AddressDto> addressDtos = getAddressDto();

        return addressDtos.stream()
                .map(
                        (addressDto) -> new ModelMapper().map(addressDto, AddressRequestModel.class)
                ).collect(Collectors.toList());
    }
}
