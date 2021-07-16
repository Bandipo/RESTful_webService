package com.restapi.mobileappws.ui.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;

}
