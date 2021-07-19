package com.restapi.mobileappws.ui.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserLoginRequestModel {

    @Email
    private  String email;

    @NotNull(message="Password is a required field")
    @Size(min=8, max=16, message="Password must be equal to or greater than 8 characters and less than 16 characters")
    private String password;
}
