package com.restapi.mobileappws.service.implementations;

import com.restapi.mobileappws.SharedDto.AddressDto;
import com.restapi.mobileappws.entity.AddressEntity;
import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.exceptions.AddressServiceException;
import com.restapi.mobileappws.repositories.AddressRepository;
import com.restapi.mobileappws.repositories.UserRepository;
import com.restapi.mobileappws.service.AddressService;
import com.restapi.mobileappws.ui.ErrorMessages;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

        private final AddressRepository addressRepository;
        private final UserRepository userRepository;
        private final ModelMapper modelMapper;

    @Override
    public List<AddressDto> getUserAddresses(String id) {

        UserEntity user = userRepository.findByUserId(id).orElseThrow(
                () -> new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND.getErrorMessage())
        );



        List<AddressDto> returnedDtoOfAddresses = new ArrayList<>();

        List<AddressEntity> addresses = addressRepository.findAddressEntitiesByUserDetails(user)
                .orElseThrow(
                        () -> new AddressServiceException(ErrorMessages.ADDRESS_OR_ADDRESSES_NOT_FOUND.getErrorMessage())
                );

        addresses.forEach(
                (addressEntity)->{
                    AddressDto mappedAddressDto = modelMapper.map(addressEntity, AddressDto.class);
                    returnedDtoOfAddresses.add(mappedAddressDto);
                }
        );


        return returnedDtoOfAddresses;


    }

    @Override
    public AddressDto getAddressByAddressId(String addressId) {

        AddressEntity addressEntity = addressRepository.findAddressEntityByAddressId(addressId)
                .orElseThrow(
                        () -> new AddressServiceException(ErrorMessages.ADDRESS_OR_ADDRESSES_NOT_FOUND.getErrorMessage())
                );

        return modelMapper.map(addressEntity, AddressDto.class);
    }
}
