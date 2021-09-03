package com.restapi.mobileappws.setups;

import com.restapi.mobileappws.SharedDto.Authorities;
import com.restapi.mobileappws.SharedDto.Roles;
import com.restapi.mobileappws.entity.AuthorityEntity;
import com.restapi.mobileappws.entity.RolesEntity;
import com.restapi.mobileappws.entity.UserEntity;
import com.restapi.mobileappws.repositories.AuthorityRepository;
import com.restapi.mobileappws.repositories.RoleRepository;
import com.restapi.mobileappws.repositories.UserRepository;
import com.restapi.mobileappws.utils.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Transactional
@Component
@Slf4j
@RequiredArgsConstructor
public class InitialUserSetup {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final Utility utils;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    @EventListener//listens to when application has started
    //This method only runs after Spring has registered all it beans
    public void onApplicationEvent(ApplicationReadyEvent event){
        log.debug("From Application ready event....");

        AuthorityEntity readAuthority = createAuthorities(Authorities.READ_AUTHORITY.name());
        AuthorityEntity writeAuthority = createAuthorities(Authorities.WRITE_AUTHORITY.name());
        AuthorityEntity deleteAuthority = createAuthorities(Authorities.DELETE_AUTHORITY.name());

        RolesEntity roleAdmin = createRoles(Roles.ROLE_ADMIN.name(), List.of(readAuthority, writeAuthority, deleteAuthority ));

        if(roleAdmin == null) return;

        if(userRepository.existsUserEntitiesByEmail("admin@gmail.com")){
            return;
        }

        UserEntity adminUser = new UserEntity();
        adminUser.setFirstName("admin");
        adminUser.setLastName("Decagon");
        adminUser.setEmail("admin@gmail.com");
        adminUser.setEmailVerificationStatus(true);
        adminUser.setUserId(utils.generateUserId(25));
        adminUser.setEncryptedPassword(passwordEncoder.encode("admin12345"));
        adminUser.setRoles(List.of(roleAdmin));

        userRepository.save(adminUser);



    }


    private AuthorityEntity createAuthorities(String name){

        Optional<AuthorityEntity> authority = authorityRepository.findAuthorityEntityByName(name);

        if(authority.isEmpty()){
        return authorityRepository.save(new AuthorityEntity(name));
        }

        return authority.get();

    }

    private RolesEntity createRoles(String name, Collection<AuthorityEntity> authorities ){

        Optional<RolesEntity> role = roleRepository.findRolesEntityByName(name);

        if(role.isEmpty()){
            return roleRepository.save(new RolesEntity(name, authorities));
        }

        return role.get();

    }


}
