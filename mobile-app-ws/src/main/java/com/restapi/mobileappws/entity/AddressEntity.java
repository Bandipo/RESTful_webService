package com.restapi.mobileappws.entity;

import com.restapi.mobileappws.SharedDto.UserDto;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity(name = "addresses")
public class AddressEntity implements Serializable {

    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 885672773868288126L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String addressId;
    private String streetName;
    private String city;
    private String country;
    private String postalCode;
    private String type;

    @ManyToOne()
    @JoinColumn(name = "users_id") // the format is usersTableName_id
    private UserEntity userDetails;
}
