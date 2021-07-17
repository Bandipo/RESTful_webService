package com.restapi.mobileappws.service.implementations;

import com.restapi.mobileappws.SharedDto.UserDto;
import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.repositories.UserRepository;
import com.restapi.mobileappws.service.UserService;
import com.restapi.mobileappws.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Utility utils;


    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalStateException("User already exist");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId(25);
        userEntity.setUserId(publicUserId);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));



        UserEntity savedUser = userRepository.save(userEntity);

        UserDto returnedUser = new UserDto();

        BeanUtils.copyProperties(savedUser,returnedUser);



        return returnedUser;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity user = userRepository.findByEmail(email).
                orElseThrow(
                        ()-> new UsernameNotFoundException(email)
                );
        UserDto returnValue = new UserDto();

        BeanUtils.copyProperties(user, returnValue);


        return returnValue;
    }

    @Override
    public UserDto getUserById(String id) {

        UserEntity user = userRepository.findByUserId(id)
                .orElseThrow(
                        ()-> new UsernameNotFoundException("User is not found")
                );

        UserDto returnedUser = new UserDto();

        BeanUtils.copyProperties(user, returnedUser);



        return returnedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(
                        ()-> new UsernameNotFoundException("Email or Password Incorrect")
                );
        return  new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
    }
}
