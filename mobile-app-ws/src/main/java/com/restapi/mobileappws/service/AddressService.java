package com.restapi.mobileappws.service;

import com.restapi.mobileappws.SharedDto.AddressDto;

import java.util.List;

public interface AddressService {
     List<AddressDto> getUserAddresses(String id);
     AddressDto getAddressByAddressId(String addressId);

}
