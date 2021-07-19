package com.restapi.mobileappws.ui.model.response;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessage {

    private Date timestamp;
    private String message;
}
