package com.restapi.mobileappws.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Global level configuration of Cors
//we also need to configure it in Spring security
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// this allows access to all the controller methods
                .allowedMethods("*")//it could be specific : "Put", "GET"....
                .allowedOrigins("*");// a specific origin could be specified eg: "https://localhost:8993
    }
}
