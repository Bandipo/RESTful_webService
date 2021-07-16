package com.restapi.mobileappws.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity(name = "users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -741673831079720623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


}
