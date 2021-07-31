package com.restapi.mobileappws.repositories;

import com.restapi.mobileappws.entity.AddressEntity;
import com.restapi.mobileappws.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    Optional<List<AddressEntity>> findAddressEntitiesByUserDetails (UserEntity user);

    Optional<AddressEntity> findAddressEntityByAddressId(String addressId);
}
