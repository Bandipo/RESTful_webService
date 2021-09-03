package com.restapi.mobileappws.service.implementations;

import com.restapi.mobileappws.SharedDto.AddressDto;
import com.restapi.mobileappws.SharedDto.RoleDto;
import com.restapi.mobileappws.SharedDto.Roles;
import com.restapi.mobileappws.SharedDto.UserDto;
import com.restapi.mobileappws.entity.AuthorityEntity;
import com.restapi.mobileappws.entity.RolesEntity;
import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.exceptions.UserServiceException;
import com.restapi.mobileappws.repositories.UserRepository;
import com.restapi.mobileappws.security.UserPrincipal;
import com.restapi.mobileappws.security.UserPrincipal;
import com.restapi.mobileappws.service.UserService;
import com.restapi.mobileappws.ui.ErrorMessages;
import com.restapi.mobileappws.utils.Utility;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Utility utils;
//    private final AmazonSES amazonSES;


    @Override
    public UserDto createUser(UserDto user) {

        //checks if the user is already in the database
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        }

        //sets the userDto on each Address on the fly; also sets the generated address entity

        for(int i=0; i<user.getAddresses().size(); i++){

            AddressDto addressDto = user.getAddresses().get(i);

            addressDto.setUserDetails(user);

            addressDto.setAddressId(utils.generateAddressId(25));

            user.getAddresses().set(i, addressDto);

        }

        user.setRoles(List.of(new RoleDto(Roles.ROLE_USER.name())));// set role to every user

        // copies the userDto into the UserEntity
        UserEntity userEntity = new ModelMapper().map(user, UserEntity.class);

        //generate UserId before saving the User
        String publicUserId = utils.generateUserId(25);

        //sets the generated Userid
        userEntity.setUserId(publicUserId);

        //Encrypts the password
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        //Generate and set email verification token
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        //set emailVerification Status
        userEntity.setEmailVerificationStatus(false);


        //then save the user
        UserEntity savedUser = userRepository.save(userEntity);

        //copy the saved user into a UserDto to the controller
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
        //get the user to be updated

        System.out.printf("\n updatedUser called in service request : %s ", userDto.getFirstName());
        System.out.printf("\n In updaterUser Service \n UserId from request: %s", userId);

        System.out.println( userRepository.existsUserByUserId(userId));

        UserEntity foundUser = userRepository.findByUserId(userId).
                orElseThrow(
                        () -> new UserServiceException(ErrorMessages.USER_NOT_FOUND.getErrorMessage())
                );


        System.out.printf("user is found: %s", foundUser.getFirstName());

        //set the fields to be updated

        foundUser.setFirstName(userDto.getFirstName());
        foundUser.setLastName(userDto.getLastName());

        //save the user or add @Transactional on the Class, which allows automatic persistence
        UserEntity savedUser = userRepository.save(foundUser);
        UserDto returnedUserDto = new UserDto();

        returnedUserDto.setUserId(savedUser.getUserId());
        returnedUserDto.setEmail(savedUser.getEmail());
        returnedUserDto.setFirstName(savedUser.getFirstName());
        returnedUserDto.setLastName(savedUser.getLastName());

        List<AddressDto> addressDto = savedUser.getAddresses().stream().map(
                (addresses) -> {
                 return new ModelMapper().map(addresses, AddressDto.class);
                }
        ).collect(Collectors.toList());

        returnedUserDto .setAddresses(addressDto);


        return returnedUserDto;
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


    //Spring framework calls this function when post request is send for user Authentication

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(
                        ()-> new UsernameNotFoundException(ErrorMessages.INCORRECT_EMAIL_OR_PASSWORD.getErrorMessage())
                );
        //user.getEmailVerificationStatus() replaces isEnabled of UserDetails

//        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

       /* user.getRoles().stream().map(
                RolesEntity::getAuthorities).forEach(
                       authorities -> {
                           authorities.forEach(
                                   authorityEntity -> grantedAuthorities.add(new SimpleGrantedAuthority(authorityEntity.getName()))
                           );

                       }
        );*/


        return new UserPrincipal(user);



//        return new User(user.getEmail(), user.getEncryptedPassword(), user.getEmailVerificationStatus(), true,
//                true, true, grantedAuthorities);
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
