package com.restapi.mobileappws.service.implementations;

import com.restapi.mobileappws.SharedDto.UserDto;
import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.exceptions.UserServiceException;
import com.restapi.mobileappws.repositories.UserRepository;
import com.restapi.mobileappws.service.UserService;
import com.restapi.mobileappws.ui.ErrorMessages;
import com.restapi.mobileappws.utils.Utility;
import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Utility utils;


    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        }



        UserEntity userEntity = new ModelMapper().map(user, UserEntity.class);

        String publicUserId = utils.generateUserId(25);
        userEntity.setUserId(publicUserId);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));



        UserEntity savedUser = userRepository.save(userEntity);


        return new ModelMapper().map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUser(String email) {

        UserEntity user = userRepository.findByEmail(email).
                orElseThrow(
                        ()-> new UsernameNotFoundException(email)
                );


        return new ModelMapper().map(user, UserDto.class);
    }

    @Override
    public UserDto getUserById(String id) {

        UserEntity user = userRepository.findByUserId(id)
                .orElseThrow(
                        ()-> new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage())
                );

        return new ModelMapper().map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {

        UserEntity foundUser = userRepository.findByUserId(userId).
                orElseThrow(
                        () -> new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND.getErrorMessage())
                );

        foundUser.setFirstName(userDto.getFirstName());
        foundUser.setLastName(userDto.getLastName());

        UserEntity savedUser = userRepository.save(foundUser);
        UserDto returnedUseDto = new UserDto();

        returnedUseDto.setUserId(savedUser.getUserId());
        returnedUseDto.setEmail(savedUser.getEmail());
        returnedUseDto.setFirstName(savedUser.getFirstName());
        returnedUseDto.setLastName(savedUser.getLastName());






        return returnedUseDto;
    }


    @Transactional
    @Override
    public void deleteUser(String id) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(id);
        if(userEntityOptional.isEmpty()){
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        userRepository.deleteUserEntityByUserId(id);
    }

    @Override
    public List<UserDto> getListOfUsers(int page , int limit) {

        Pageable pageableRequest = PageRequest.of(page, limit);

        Page<UserEntity> userEntitiesPage = userRepository.findAll(pageableRequest);

        List<UserEntity> userEntities = userEntitiesPage.getContent();

        List<UserDto> userDtos = userEntities.stream().map(
                (userEntity) -> {
                    return new ModelMapper().map(userEntity, UserDto.class);

                }
        ).collect(Collectors.toList());

        return userDtos;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(
                        ()-> new UsernameNotFoundException(ErrorMessages.INCORRECT_EMAIL_OR_PASSWORD.getErrorMessage())
                );
        return  new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
    }



}
