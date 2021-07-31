package com.restapi.mobileappws.security;

import com.restapi.mobileappws.SpringApplicationContext;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 864000000; //10days
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_String = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
//    public static final String TOKEN_SECRET ="jf9i4jgu88nf10";


    public static String getTokenSecret(){
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }

}
