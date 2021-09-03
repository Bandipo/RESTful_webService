package com.restapi.mobileappws.repositories;

import com.restapi.mobileappws.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
   Optional< UserEntity> findByEmail(String email);
   Optional<UserEntity> findByUserId(String UserId);

    Boolean existsUserByUserId(String id);
    void deleteUserEntityByUserId(String id);




    Optional<UserEntity> findUserEntityByEmailVerificationToken(String token);

    //Using Native Query language
    // the table and column fields must match the ones in the database

    //countQuery: we only use count query when working with Pageable

    @Query(value ="select * from users u where u.email_verification_status ='true' ", nativeQuery = true,
    countQuery = "select count(*) from users u where u.email_verification_status ='true' " )
    Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);


    //using positional argument
    // the order of the argurment must match the placeholders
    @Query(value ="select * from users u where u.first_name = ?1 " +
            "and u.last_name =?2 order by u.first_name, u.last_name", nativeQuery = true)
    List<UserEntity> findUsersByFirstNameAndLastName(String firstName, String lastName);

   //using named Param
  // The parameter value must match the :value looked for
 // order of
    @Query(value = "select * from users u where u.last_name = :lastName", nativeQuery = true)
    List<UserEntity> findUsersByLastName(@Param(value = "lastName") String lastName);


    //Using the LIKE for matching queries

    @Query(value="select * from users u where first_name LIKE '%:keyword%' or last_name LIKE  '%:keyword%'",nativeQuery=true)
    List<UserEntity> findUsersByKeyword(@Param("keyword") String keyword);

    @Transactional
    @Modifying
    @Query(value = "update users u set u.email_verification_status =:emailVerificationStatus where u.user_Id = :userId", nativeQuery = true)
    void updateUserEmailVerificationStatus(@Param(value = "emailVerificationStatus") boolean emailVerificationStatus, @Param(value = "userId") String userId);


    //Jpql: with jpql you can write queries that are independent of database venddors
    // this makes it easy to switch from one database to another
  //Jpql uses Database EntityClass  and fields instead of table name or columns

    // for jpql to work we must annotate the entity class with @Table(name = name)

    @Query("select user from UserEntity user where user.userId = :userId")
   Optional<UserEntity> findUserByUserId (@Param(value="userId") String userId);

    @Transactional
    @Modifying
    @Query("update UserEntity user set user.firstName = :firstName where user.userId= :userId")
    void updateUserFirstName(@Param(value = "firstName") String firstName, @Param(value = "userId") String userId);

    boolean existsUserEntitiesByEmail(String email);
}
