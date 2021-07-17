package com.restapi.mobileappws.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class AppProperties {

    @Autowired
    private Environment env;


    public String getTokenSecret(){
        return env.getProperty("tokenSecret");
    }


}
