package com.restapi.mobileappws.SharedDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class AddressDto {

    private Long id;

    private String addressId;

    private String city;

    private String country;

    private String streetName;

    private String postalCode;

    private String type;


    private UserDto userDetails;


}
