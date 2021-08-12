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

    @Column(length = 30, nullable = false)
    private String addressId;

    @Column(length = 15, nullable = false)
    private String city;

    @Column(length = 15, nullable = false)
    private String country;

    @Column(length = 100, nullable = false)
    private String streetName;

    @Column(length =10 , nullable = false)
    private String postalCode;

    @Column(length = 15, nullable = false)
    private String type;

    private UserDto userDetails;
}
