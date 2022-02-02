package com.restapi.mobileappws.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.mobileappws.SharedDto.UserDto;
import com.restapi.mobileappws.SpringApplicationContext;
import com.restapi.mobileappws.service.UserService;
import com.restapi.mobileappws.ui.model.request.UserLoginRequestModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;




    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)

                                                throws AuthenticationException {



// before authentication, we have to get the credentials from the request...
        // copy the credentials to UserLoginReqestModel
        try{
            UserLoginRequestModel credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequestModel.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            new ArrayList<>()
                    )
            );//Spring frame work validates user name and password against the one
            //we av in database


        }catch (IOException e){
            throw new RuntimeException(e);

        }


    }


    //if user is authenticated this function is called
    //here we generate jwt token
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
                                            throws IOException, ServletException {


        String userName = ((UserPrincipal) authResult.getPrincipal()).getUsername(); //user's email



        //String tokenSecret = new SecurityConstants.getTokenSecret();

        //Generate token
        String token = Jwts.builder()
                       .setSubject(userName) //the subject is user's email
                       .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                       .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                       .compact();

        //this gets userServiceImp Bean from Application context class
        //we can get any registered spring bean with SpringApplicationContext

        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");

        UserDto userDto = userService.getUser(userName);// we get the user by his email

        //set the token and userId in the response Header

        response.addHeader(SecurityConstants.HEADER_String, SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader("UserID", userDto.getUserId()); //



    }
}
