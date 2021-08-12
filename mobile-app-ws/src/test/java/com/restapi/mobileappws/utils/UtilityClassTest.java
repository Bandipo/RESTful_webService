package com.restapi.mobileappws.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)// This helps to load Spring context;
@SpringBootTest
@Slf4j
class UtilityClassTest {

    @Autowired
    Utility utility;


    @BeforeEach
    void setUp() {
    }

    @Test
    void generateUserId() {
        String userId = utility.generateUserId(25);
        String userId2 = utility.generateUserId(25);

        log.info("userId1: {}", userId);
        log.info("userId2 {}", userId2);

       assertAll(
               ()->{
                   assertNotNull(userId);
                   assertEquals(25, userId.length());
                   assertFalse(userId.equalsIgnoreCase(userId2));
               }
       );
    }

//    @Test
//    void hasTokenNotExpired() {
//        String token = utility.generateEmailVerificationToken("3234dfjkld");
//
//        Boolean hasTokenExpired = Utility.hasTokenExpired(token);
//
//        assertAll(
//                ()->{
//                    assertNotNull(token);
//                    assertFalse(hasTokenExpired);
//                }
//        );
//    }

//    @Test
//    @Disabled
//     void hasTokenExpired(){
//        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MUB0ZXN0LmNvbSIsImV4cCI6MTUzMjc3Nzc3NX0." +
//                "cdudUo3pwZLN9UiTuXiT7itpaQs6BgUPU0yWbNcz56-l1Z0476N3H_qSEHXQI5lUfaK2ePtTWJfROmf0213UJA";
//
//        Boolean hasTokenExpired = Utility.hasTokenExpired(expiredToken);
//
//        assertTrue(hasTokenExpired);
//
//    }
}