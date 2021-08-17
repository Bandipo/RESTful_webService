package com.restapi.mobileappws.service.implementations;

import com.restapi.mobileappws.SharedDto.AddressDto;
import com.restapi.mobileappws.SharedDto.UserDto;
import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.exceptions.UserServiceException;
import com.restapi.mobileappws.repositories.UserRepository;
import com.restapi.mobileappws.service.UserService;
import com.restapi.mobileappws.ui.ErrorMessages;
import com.restapi.mobileappws.utils.Utility;
import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;

import org.springframework.beans.BeanUtils;
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
//    private final AmazonSES amazonSES;


    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        }

        for(int i=0; i<user.getAddresses().size(); i++){

            AddressDto addressDto = user.getAddresses().get(i);

            addressDto.setUserDetails(user);

            addressDto.setAddressId(utils.generateAddressId(25));

            user.getAddresses().set(i, addressDto);

        }



        UserEntity userEntity = new ModelMapper().map(user, UserEntity.class);

        String publicUserId = utils.generateUserId(25);

        userEntity.setUserId(publicUserId);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(false);



        UserEntity savedUser = userRepository.save(userEntity);

        UserDto returnValue = new ModelMapper().map(savedUser, UserDto.class);

//        // Send an email message to user to verify their email address
//        amazonSES.verifyEmail(returnValue);

        return returnValue ;
    }

    @Override
    public UserDto getUser(String email) {

        UserEntity user = userRepository.findByEmail(email).
                orElseThrow(
                        ()-> new UsernameNotFoundException(email)
                );
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(user, userDto);

        return userDto;
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

        List<AddressDto> addressDto = savedUser.getAddresses().stream().map(
                (addresses) -> {
                 return new ModelMapper().map(addresses, AddressDto.class);
                }
        ).collect(Collectors.toList());

        returnedUseDto.setAddresses(addressDto);




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
        return new User(user.getEmail(), user.getEncryptedPassword(), user.getEmailVerificationStatus(), true,
                true, true, new ArrayList<>());
//        return  new User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
    }


    @Override
    public Boolean verifyEmailToken(String token) {
        boolean returnValue = false;

        //first find if the user exists

        UserEntity user = userRepository.findUserEntityByEmailVerificationToken(token)
                .orElseThrow(
                        () -> new UserServiceException(ErrorMessages.USER_NOT_FOUND.getErrorMessage())
                );

        //check if the token is still valid

        Boolean hasTokenExpired  = Utility.hasTokenExpired(token);

        //returns true if the token has expired

        if(!hasTokenExpired){
            user.setEmailVerificationToken(null); // we delete the emailverification token
            user.setEmailVerificationStatus(true);// the email is verified
            userRepository.save(user); //save the user back
            returnValue = true;
        }

        return returnValue;
    }



}
