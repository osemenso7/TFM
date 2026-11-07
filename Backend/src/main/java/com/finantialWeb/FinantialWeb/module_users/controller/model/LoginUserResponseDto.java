package com.finantialWeb.FinantialWeb.module_users.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserResponseDto {

    private UserDto user;
    private String token;
}