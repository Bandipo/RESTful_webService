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

    @Column(length = 30, nullable = false)
    private String addressId;

    @Column(length = 100, nullable = false)
    private String streetName;

    @Column(length = 15, nullable = false)
    private String city;

    @Column(length = 15, nullable = false)
    private String country;

    @Column(length =10 , nullable = false)
    private String postalCode;

    @Column(length = 15, nullable = false)

    private String type;

    @ManyToOne()
    @JoinColumn(name = "users_id") // the format is usersTableName_id
    private UserEntity userDetails;
}
