package com.restapi.mobileappws.controller;

import com.restapi.mobileappws.SharedDto.UserDto;
import com.restapi.mobileappws.service.UserService;
import com.restapi.mobileappws.ui.model.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;


    String userId = "djksjkd89382";
    UserDto userDto;


    @BeforeEach
    void setUp() {

        userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setEmail("bandipotaiye@gmail.com");
        userDto.setLastName("Bandipo");
        userDto.setFirstName("Taiye");
    }

    @Test
    void getUser() {

        //when
        when(userService.getUserById(anyString())).thenReturn(userDto);

        ResponseEntity<UserResponse> user = userController.getUser(userId);

        assertTrue(user.getStatusCode().is2xxSuccessful());
        assertNotNull(user);
        assertEquals(user.getBody().getUserId(), userDto.getUserId());
        assertEquals(user.getBody().getFirstName(), userDto.getFirstName());
    }
}