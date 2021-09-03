package com.restapi.mobileappws.repositories;

import com.restapi.mobileappws.entity.RolesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RolesEntity, Long> {

    Optional<RolesEntity> findRolesEntityByName (String roleName);

}
