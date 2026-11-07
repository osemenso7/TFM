package com.finantialWeb.FinantialWeb.module_users.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSecurityData {
    String username;
    long tokenVersion;
}
