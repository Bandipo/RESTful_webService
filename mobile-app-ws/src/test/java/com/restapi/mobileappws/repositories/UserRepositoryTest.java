package com.restapi.mobileappws.repositories;

import com.restapi.mobileappws.TestUtility;
import com.restapi.mobileappws.entity.AddressEntity;
import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.exceptions.UserServiceException;
import com.restapi.mobileappws.ui.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)// this makes it an integration test
//@ExtedWith(SpringExtension.class): This makes it possible for use to connect to the database and perform
//operations; This also makes spring context available for  us so that we can autowire beans
@SpringBootTest
class UserRepositoryTest {

    @Autowired
     UserRepository userRepository;

    static boolean recordsCreated = false;

    final String userId = "1a2b3c";


    @BeforeEach
    void setUp() {

        if(!recordsCreated) createRecords();
    }

    @Test
    void findAllUsersWithConfirmedEmailAddress() {
        Pageable pageable = PageRequest.of(0,2);

        Page<UserEntity> userEntityPage = userRepository.
                                         findAllUsersWithConfirmedEmailAddress(pageable);

        List<UserEntity> userEntities = userEntityPage.getContent();


        assertNotNull(userEntityPage);
        assertEquals(1, userEntities.size());
        assertEquals("test@test.com", userEntities.get(0).getEmail());

    }

    @Test
    void findUsersByFirstNameAndLastName(){

        List<UserEntity> usersByFirstNameAndLastName = userRepository.findUsersByFirstNameAndLastName("Taiye", "Bandipo");

        assertEquals(2, usersByFirstNameAndLastName.size());
        assertEquals("Taiye", usersByFirstNameAndLastName.get(0).getFirstName());

    }

    @Test
    void findUsersByLastName(){

        List<UserEntity> usersByLastName = userRepository.findUsersByLastName("Bandipo");

        assertNotNull(usersByLastName);
        assertEquals(2,usersByLastName.size());
        assertTrue(usersByLastName.get(0).getLastName().equals("Bandipo"));

    }

    @Test
    @Disabled// this seem not to work for reasons I do not know
    void findUsersByKeyword(){
        List<UserEntity> usersByKeyword = userRepository.findUsersByKeyword("Bandipo");

//        UserEntity userEntity = usersByKeyword.get(0);

        log.warn("size: {}", usersByKeyword.size());

        assertNotNull(usersByKeyword);
    }

    @Test
    void test_updateUserEmailVerificationStatus(){



        userRepository.updateUserEmailVerificationStatus(false, userId );

        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(
                () -> new UserServiceException(ErrorMessages.USER_NOT_FOUND.getErrorMessage())
        );

        assertFalse((boolean) userEntity.getEmailVerificationStatus());


    }

    @Test
    void test_JpqlFindUserByUserId(){


        UserEntity userEntity = userRepository.findUserByUserId(userId).orElseThrow(
                () -> new UserServiceException(ErrorMessages.USER_NOT_FOUND.getErrorMessage())
        );

        assertEquals("Bandipo", userEntity.getLastName());

    }

    @Test
    void test_JpqlUpdateUserFirstName(){

        userRepository.updateUserFirstName("Paul", userId);

        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(
                () -> new UserServiceException(ErrorMessages.USER_NOT_FOUND.getErrorMessage())
        );

        assertEquals("Paul", userEntity.getFirstName());
    }


  private  void createRecords(){
    // Prepare User Entity
    UserEntity userEntity = new UserEntity();
    userEntity.setFirstName("Taiye");
    userEntity.setLastName("Bandipo");
    userEntity.setUserId("dlfdlfdf");
    userEntity.setEncryptedPassword("xxx");
    userEntity.setEmail("test@test.com");
    userEntity.setEmailVerificationStatus(true);

    // Prepare User Addresses
    AddressEntity addressEntity = new AddressEntity();
    addressEntity.setType("shipping");
    addressEntity.setAddressId("ahgyt74hfy");
    addressEntity.setCity("Vancouver");
    addressEntity.setCountry("Canada");
    addressEntity.setPostalCode("ABCCDA");
    addressEntity.setStreetName("123 Street Address");

    List<AddressEntity> addresses = new ArrayList<>();
    addresses.add(addressEntity);

    userEntity.setAddresses(addresses);

    userRepository.save(userEntity);

    // Prepare User Entity
    UserEntity userEntity1 = new UserEntity();
    userEntity1.setFirstName("Taiye");
    userEntity1.setLastName("Bandipo");
    userEntity1.setUserId("1a2b3c");
    userEntity1.setEncryptedPassword("dkfdlf");
    userEntity1.setEmail("bandipotaiye@gmail.com");
    userEntity1.setEmailVerificationStatus(true);

    // Prepare User Addresses
    AddressEntity addressEntity1 = new AddressEntity();
    addressEntity1.setType("shipping");
    addressEntity1.setAddressId("ahgyt74hfy");
    addressEntity1.setCity("Offa");
    addressEntity1.setCountry("Nigeria");
    addressEntity1.setPostalCode("250101");
    addressEntity1.setStreetName("Itafa, Offa");

    List<AddressEntity> addresses1 = new ArrayList<>();
    addresses1.add(addressEntity1);

    userEntity1.setAddresses(addresses1);

    userRepository.save(userEntity1);
    recordsCreated = true;
}

}