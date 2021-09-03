package com.restapi.mobileappws.security;

import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.exceptions.UserServiceException;
import com.restapi.mobileappws.repositories.UserRepository;
import com.restapi.mobileappws.ui.ErrorMessages;
import io.jsonwebtoken.Jwts;


import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//for every request after user authentication
@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {
    private final UserRepository userRepository;
    public AuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;


    }




    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        //we validate the token

        String header = request.getHeader(SecurityConstants.HEADER_String);

        if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request,response);


    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){


        String token = request.getHeader(SecurityConstants.HEADER_String);

        //we get the token from the header
        if(token != null){
            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

            //get user email = username from the token
            String username = Jwts.parser()
                    .setSigningKey(SecurityConstants.getTokenSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            if(username != null){

                UserEntity user = userRepository.findByEmail(username).orElseThrow(
                        () -> new UserServiceException(ErrorMessages.USER_NOT_FOUND.getErrorMessage())
                );

                if(user==null) return null;

                UserPrincipal principal = new UserPrincipal(user);




                log.warn("user Authourities: {} ", principal.getAuthorities());//TODO : remove
                log.info("username: {}", principal.getUsername());

                return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            }
            return  null;

        }

        return  null;

    }

}
