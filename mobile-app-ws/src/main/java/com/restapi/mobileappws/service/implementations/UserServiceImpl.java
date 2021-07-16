package com.restapi.mobileappws.service.implementations;

import com.restapi.mobileappws.SharedDto.UserDto;
import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.repositories.UserRepository;
import com.restapi.mobileappws.service.UserService;
import com.restapi.mobileappws.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Utility utils;


    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new IllegalStateException("User already exist");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId(25);



        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword("test");



        UserEntity savedUser = userRepository.save(userEntity);

        UserDto returnedUser = new UserDto();

        BeanUtils.copyProperties(savedUser,returnedUser);



        return returnedUser;
    }
}
