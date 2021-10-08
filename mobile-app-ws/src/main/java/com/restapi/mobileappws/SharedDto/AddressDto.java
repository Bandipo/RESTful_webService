package com.restapi.mobileappws.SharedDto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@NoArgsConstructor
@Getter@Setter
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
