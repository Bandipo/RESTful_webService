package com.restapi.mobileappws;

import com.restapi.mobileappws.SharedDto.AddressDto;
import com.restapi.mobileappws.entity.AddressEntity;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtility {

    private static  List<AddressDto> getAddressDto(){
        AddressDto addressDto = new AddressDto();
        addressDto.setType("shipping");
        addressDto.setId(1L);
        addressDto.setCity("Lagos");
        addressDto.setCountry("Nigeria");
        addressDto.setPostalCode("250111");

        AddressDto billingAddressDto = new AddressDto();

        billingAddressDto.setType("shipping");
        billingAddressDto.setId(1L);
        billingAddressDto.setCity("Lagos");
        billingAddressDto.setCountry("Nigeria");
        billingAddressDto.setPostalCode("250111");

        List<AddressDto> addresses = new ArrayList<>();

        addresses.add(addressDto);
        addresses.add(billingAddressDto);
        return addresses;
    }


    public static List<AddressEntity> getAddressEntity(){

        List<AddressDto> addressDtos = getAddressDto();

        return addressDtos.stream()
                .map(
                        (addressDto) -> new ModelMapper().map(addressDto, AddressEntity.class)
                ).collect(Collectors.toList());
    }
}
