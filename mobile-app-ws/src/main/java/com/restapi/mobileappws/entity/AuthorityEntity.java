package com.restapi.mobileappws.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
@Entity
//@Table(name = "authorities")
@NoArgsConstructor
@Getter @Setter
public class AuthorityEntity implements Serializable {

   @Getter(AccessLevel.NONE)@Setter(AccessLevel.NONE)
    private static final long serialVersionUID = -8139890146219040253L;

    public AuthorityEntity(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

 public String getName() {
  return name;
 }

 @Column(nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "authorities")//the field from the userEntity
    private Collection<RolesEntity> roles;
}
