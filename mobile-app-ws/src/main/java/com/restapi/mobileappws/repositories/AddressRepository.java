package com.restapi.mobileappws.repositories;

import com.restapi.mobileappws.entity.AddressEntity;
import com.restapi.mobileappws.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findAddressEntitiesByUserDetails(UserEntity user);
    AddressEntity findAddressEntityByAddressIdAndUserDetails(UserEntity user, String addressId);

    AddressEntity findAddressEntityByAddressId(String addressId);

}
