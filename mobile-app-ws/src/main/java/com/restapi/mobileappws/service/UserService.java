package com.restapi.mobileappws.service;

import com.restapi.mobileappws.SharedDto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);
    UserDto getUser(String email);

    UserDto getUserById(String id);

    UserDto updateUser(String userId, UserDto userDto);

    void deleteUser(String id);
}
