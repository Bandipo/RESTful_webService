package com.restapi.mobileappws.ui.model.response;

import com.restapi.mobileappws.SharedDto.AddressDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserResponse {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;

    List<AddressResponse> addresses;

}
