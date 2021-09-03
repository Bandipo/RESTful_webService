package com.restapi.mobileappws.repositories;

import com.restapi.mobileappws.entity.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {
    Optional<AuthorityEntity> findAuthorityEntityByName (String name);
}
