package com.restapi.mobileappws.utils;

import com.restapi.mobileappws.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Component
public class Utility {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopwxyz";
    private final int ITERATIONS = 10000;
    private final int KEY_LENGTH = 256;




    public String generateUserId(int length){
        return generateRandomString(length);
    }

    public String generateAddressId(int length){
        return generateRandomString(length);
    }

    private String generateRandomString(int length){
        StringBuilder returnValue = new StringBuilder(length);

        for(int i = 0; i<length ; i++){
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));

        }

        return new String(returnValue);
    }

      @Bean
    public static ModelMapper modelMapper(){
        return new ModelMapper();
    }


    public static Boolean hasTokenExpired(String token) {
        Claims claims = Jwts.parser()
                        .setSigningKey(SecurityConstants.getTokenSecret())
                        .parseClaimsJws(token).getBody();

        Date tokenExpirationDate = claims.getExpiration();
        Date todayDate = new Date();

        return  tokenExpirationDate.before(todayDate);


    }

    public String generateEmailVerificationToken(String publicUserId) {
        String token = Jwts.builder()
                       .setSubject(publicUserId)
                       .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                       .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                       .compact();
        return token;
    }
}
