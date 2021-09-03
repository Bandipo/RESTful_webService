package com.restapi.mobileappws.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
//@Table(name = "roles")
@Getter@Setter@NoArgsConstructor
public class RolesEntity implements Serializable {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = -8139890146219040253L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "roles")//the field from the userEntity
    private Collection<UserEntity> users;


    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name="roles_authorities",
            joinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authorities_id", referencedColumnName = "id") )
    private Collection<AuthorityEntity> authorities;

    public RolesEntity(String name, Collection<AuthorityEntity> authorities) {
        this.name = name;
        this.authorities= authorities;
    }
}
