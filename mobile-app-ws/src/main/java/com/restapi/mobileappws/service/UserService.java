package com.restapi.mobileappws.service;

import com.restapi.mobileappws.SharedDto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);
    UserDto getUser(String email);

    UserDto getUserById(String id);

    UserDto updateUser(String userId, UserDto userDto);

    void deleteUser(String id);

    List<UserDto> getListOfUsers(int page, int limit);


    Boolean verifyEmailToken(String token);
}
