package com.restapi.mobileappws.repositories;

import com.restapi.mobileappws.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
   Optional< UserEntity> findByEmail(String email);
   Optional<UserEntity> findByUserId(String UserId);

    Boolean existsUserByUserId(String id);
    void deleteUserEntityByUserId(String id);

}
