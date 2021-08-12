package com.restapi.mobileappws.ui.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
public class AddressResponse extends RepresentationModel<AddressResponse> {

    private String city;
    private String addressId;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;

}
