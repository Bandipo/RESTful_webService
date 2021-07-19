package com.restapi.mobileappws.ui.model.request;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor

public class UserDetailsRequestModel {

//
//    @NotNull(message="First name cannot be missing or empty")
//    @Size(min=2, message="First name must not be less than 2 characters")
    private String firstName;

    @NotNull(message="Last name cannot be missing or empty")
    @Size(min=2, message="Last name must not be less than 2 characters")
    private String lastName;

    @Email
    private String email;

    @NotNull(message="Password is a required field")
    @Size(min=8, max=16, message="Password must be equal to or greater than 8 characters and less than 16 characters")
    private String password;

}
