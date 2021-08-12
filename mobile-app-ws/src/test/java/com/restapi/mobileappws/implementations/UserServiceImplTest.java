package com.restapi.mobileappws.implementations;

import com.restapi.mobileappws.SharedDto.AddressDto;
import com.restapi.mobileappws.SharedDto.UserDto;
import com.restapi.mobileappws.entity.AddressEntity;
import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.exceptions.UserServiceException;
import com.restapi.mobileappws.repositories.UserRepository;
import com.restapi.mobileappws.service.implementations.UserServiceImpl;
import com.restapi.mobileappws.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;



    @Mock
    UserRepository userRepository;


    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    Utility utils;


    String userId = "ydieldhdi34oskl";
    String addressId = "dkofheko234lk";
    String encryptedPassword = "$DFJAEFJKDFDJKDF#$##";
    String userEmail = "bandipotaiye@gmail.com";



    UserEntity userEntity;

    UserDto userDto;


    @BeforeEach
    void setUp() {
//        MockitoAnnotations.initMocks(this);

        //UserEntity
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Taiye");
        userEntity.setLastName("Bandipo");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail(userEmail);
        userEntity.setEmailVerificationToken("eildfj");
        userEntity.setAddresses(getAddressEntity());

        //UserDto

        userDto = new UserDto();

        userDto.setAddresses(getAddressDto());
        userDto.setEmail(userEmail);
        userDto.setPassword("taiye12345");
        userDto.setUserId(userId);
    }

    @Test
    void getUser() {

        Optional<UserEntity> optionalUserEntity = Optional.of(this.userEntity);



        //when
        when(userRepository.findByEmail(anyString())).thenReturn(optionalUserEntity );



        UserDto user = userService.getUser(userEmail);// call the method being tested


         log.info("userToGEt {}",user.getFirstName());// TODO: To be removed
         log.info("savedUser {}", optionalUserEntity.get().getFirstName());// TODO: To be removed


        //then
       assertEquals(user.getFirstName(), optionalUserEntity.get().getFirstName());


    }

    @Test
    void testGetUser_UsernameNotFoundException(){
        // userRepository.findByEmail should return an exception when user is not found

        //when
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                ()-> userService.getUser(userEmail)

                );

    }

    @Test
    void testCreateUser(){



        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(utils.generateAddressId(anyInt())).thenReturn(addressId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);









        UserDto storedUser = userService.createUser(userDto);


        //Then

        assertNotNull(storedUser);
        assertEquals(userEntity.getFirstName(), storedUser.getFirstName());
        assertNotNull(storedUser.getUserId());
        assertEquals(storedUser.getAddresses().size(), userEntity.getAddresses().size());

        verify(utils,times(1)).generateUserId(25);//TODO: ought to work for 2
        verify(bCryptPasswordEncoder,times(1)).encode("taiye12345");
        verify(utils, times(1)).generateUserId(25);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testCreateUser_IllegalStateException(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(this.userEntity));



        assertThrows(IllegalStateException.class,
                ()-> userService.createUser(userDto)
                );
    }

     @Test
     void testDeleteUser(){
     //when
   when(userRepository.findByUserId(anyString())).thenReturn(Optional.of(userEntity));

    //then
     userService.deleteUser(userId);

     verify(userRepository, times(1)).findByUserId(userId);


    }

    @Test
    void testDeleteUser_ShouldThrowUserServiceException(){
        when(userRepository.findByUserId(anyString())).thenReturn(Optional.empty());

        assertThrows(UserServiceException.class,

                ()-> userService.deleteUser(userId)

                );

    }

    @Test
    void testUpdateUser(){

        //Given


       userDto.setFirstName("Paul");
       userDto.setLastName("BAOG");


        //when
        when(userRepository.findByUserId(anyString())).thenReturn(Optional.of(userEntity));

       when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto updatedUserDto = userService.updateUser(userId, userDto);

        //Then

        assertEquals(updatedUserDto.getUserId(), userEntity.getUserId());//calls Method under Test
        verify(userRepository, times(1)).findByUserId(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testUpdateUser_shouldThrowUsernameNotFoundException(){

        //when
        when(userRepository.findByUserId(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                ()-> userService.updateUser(userId, userDto)
                );
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


    private List<AddressEntity> getAddressEntity(){

        List<AddressDto> addressDtos = getAddressDto();

        return addressDtos.stream()
                .map(
                        (addressDto) -> new ModelMapper().map(addressDto, AddressEntity.class)
                ).collect(Collectors.toList());
    }
}