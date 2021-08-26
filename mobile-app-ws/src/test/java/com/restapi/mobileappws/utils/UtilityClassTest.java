package com.restapi.mobileappws.utils;

import com.restapi.mobileappws.security.SecurityConstants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

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

    @Test
    void hasTokenNotExpired() {
        String token = utility.generateEmailVerificationToken("3234dfjkld");



        Boolean hasTokenExpired = Utility.hasTokenExpired(token);

        assertAll(
                ()->{
                    assertNotNull(token);
                    assertFalse(hasTokenExpired);
                }
        );
    }

    @Test

     void hasTokenExpired(){
        String expiredToken = generateExpiredEmailVerificationTokenForTest("dkdlf");

        Boolean hasTokenExpired = Utility.hasTokenExpired(expiredToken);

        assertTrue(hasTokenExpired);

    }


    private static String generateExpiredEmailVerificationTokenForTest(String publicUserId) {
        String token = Jwts.builder()
                .setSubject(publicUserId)
                .setExpiration(new Date(System.currentTimeMillis() - 1000))// this makes the token expire
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
        return token;

    }



}