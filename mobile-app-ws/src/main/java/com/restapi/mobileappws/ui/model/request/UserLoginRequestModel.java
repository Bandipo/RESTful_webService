package com.restapi.mobileappws.ui.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserLoginRequestModel {

    @Email
    private  String email;

    @NotNull(message="Password is a required field")
//    @Size(min=8, max=16, message="Password must be equal to or greater than 8 characters and less than 16 characters")
    @Pattern(regexp ="^(([0-9]|[a-z]|[A-Z]|[@])*){8,20}$", message = "Password must contain a digit, " +
            "lowercase, upperCase, a symbol") //TODO: copy the pattern to UserRequestModel payload
    private String password;

    // "^(([0-9]|[a-z]|[A-Z]|[@])*){8,20}$"
}
