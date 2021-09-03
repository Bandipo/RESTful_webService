package com.restapi.mobileappws.security;

import com.restapi.mobileappws.entity.AuthorityEntity;
import com.restapi.mobileappws.entity.RolesEntity;
import com.restapi.mobileappws.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@NoArgsConstructor
@Getter@Setter// also required for method level security
public class UserPrincipal implements UserDetails {

    private UserEntity user;
    private String userId;// this we make use for method level security

    public UserPrincipal(UserEntity user) {
        this.user = user;
        this.userId = user.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();//Granted authorities tobe returned

        //Get user Roles
        Collection<RolesEntity> userRoles = user.getRoles();
        //Authorities associated with each roles will also be added to the GrantedAuthorities

        Set<AuthorityEntity> userAuthorities = new HashSet<>();

        //perhaps it is possible for a user not to av roles

        if(userRoles.isEmpty()) return grantedAuthorities;

        //copy each roles into Collection of grantedAuthorities
        //copy each authorities of each role into set of userAuthorities
        userRoles.forEach(
                role->  { grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
                        userAuthorities.addAll(role.getAuthorities());//there are lists of authorities
                }
        );

        userAuthorities.forEach(userAuthority-> grantedAuthorities.add(new SimpleGrantedAuthority(userAuthority.getName())) );




        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.user.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.getEmailVerificationStatus();
    }


}
