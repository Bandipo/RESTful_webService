package com.restapi.mobileappws.exceptions;

public class AddressServiceException extends RuntimeException{

    private static final long serialVersionUID = 4707366883822672283L;

    public AddressServiceException(String message){
        super(message);
    }
}
