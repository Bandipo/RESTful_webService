package com.restapi.mobileappws.service.implementations;

import com.restapi.mobileappws.SharedDto.AddressDto;
import com.restapi.mobileappws.entity.AddressEntity;
import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.repositories.AddressRepository;
import com.restapi.mobileappws.repositories.UserRepository;
import com.restapi.mobileappws.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @InjectMocks
    AddressServiceImpl addressService;

    @Mock
    UserRepository userRepository;

    @Mock
    AddressRepository addressRepository;

     UserEntity userEntity;
     List<AddressEntity> addressEntity;

     String userId = "diduenodfd";

    @BeforeEach
    void setUp() {

        //UserEntity
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Taiye");
        userEntity.setLastName("Bandipo");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword("kdfldjldflf");
        userEntity.setEmail("user@gmail.com");
        userEntity.setEmailVerificationToken("eildfj");
        userEntity.setEmailVerificationStatus(true);
        userEntity.setAddresses(getAddressEntity());

        //AddressEntity

         addressEntity = getAddressEntity();

    }

    @Test
    void testGetUserAddresses(){

        //when

        when(userRepository.findByUserId(anyString())).thenReturn(Optional.of(userEntity));

        when(addressRepository.findAddressEntitiesByUserDetails(any(UserEntity.class))).thenReturn(addressEntity);

        //when the Method under Test is called
        List<AddressDto> addressDtos = addressService.getUserAddresses(userId);

        //Then

        assertNotNull(addressDtos);
        assertEquals(addressEntity.size(), addressDtos.size());




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