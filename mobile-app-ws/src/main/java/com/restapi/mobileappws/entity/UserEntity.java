package com.restapi.mobileappws.entity;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Data
@Entity
@Table(name = "users")
public class UserEntity implements Serializable {

    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = -741673831079720623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //the same is used at the users_roles refrenced column

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    private String encryptedPassword;
    private String emailVerificationToken;

    @Column(nullable = false)
    private Boolean emailVerificationStatus = false;


    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL) // The field that owns the relationship
    private List<AddressEntity> addresses ;

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name="users_roles",
    joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id") )
    private Collection<RolesEntity> roles;


    @Override
    public String toString() {
        return "UserEntity{" +
                "roles=" + roles +
                '}';
    }
}
