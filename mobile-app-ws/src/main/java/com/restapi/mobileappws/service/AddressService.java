package com.restapi.mobileappws.service;

import com.restapi.mobileappws.SharedDto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getUserAddresses(String userId);

    AddressDto getUserAddress(String addressId, String userId);
}
