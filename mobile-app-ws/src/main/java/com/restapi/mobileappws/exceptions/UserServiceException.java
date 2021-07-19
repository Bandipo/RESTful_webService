package com.restapi.mobileappws.exceptions;

public class UserServiceException extends RuntimeException{

    private static final long serialVersionUID = 2160480265780832964L;

    public UserServiceException(String message){
            super(message);
    }


}
