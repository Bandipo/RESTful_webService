package com.restapi.mobileappws.service.implementations;

import com.restapi.mobileappws.SharedDto.AddressDto;
import com.restapi.mobileappws.entity.AddressEntity;
import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.exceptions.UserServiceException;
import com.restapi.mobileappws.repositories.AddressRepository;
import com.restapi.mobileappws.repositories.UserRepository;
import com.restapi.mobileappws.service.AddressService;
import com.restapi.mobileappws.ui.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;


    @Override
    public List<AddressDto> getUserAddresses(String userId) {

        UserEntity foundUser = userRepository.findByUserId(userId).orElseThrow(
                () -> new UserServiceException(ErrorMessages.USER_NOT_FOUND.getErrorMessage())
        );

        List<AddressEntity> addressesByUser = addressRepository.findAddressEntitiesByUserDetails(foundUser);


        return addressesByUser.stream().map(
                (address)-> new ModelMapper().map(address, AddressDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    public AddressDto getUserAddress(String addressId, String userId) {

       Boolean userExists = userRepository.existsUserByUserId(userId);

       AddressEntity userAddress;

       if(userExists){
            userAddress = addressRepository.
                   findAddressEntityByAddressId(addressId);
       }else{
           throw new UserServiceException(ErrorMessages.USER_NOT_FOUND.getErrorMessage());
       }

        return new ModelMapper().map(userAddress, AddressDto.class);
    }
}
