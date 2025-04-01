package com.deliveryTeam.http.response;

import java.time.LocalDateTime;

import com.deliveryTeam.entity.Address;
import com.deliveryTeam.entity.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int status;
    private String message;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private String expirationTime;

    private Address address;

    private String jwt;
    private USER_ROLE role;
}
