package com.scglab.connect.services.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class LoginInfo {
    private String name;
    private String token;

    @Builder
    public LoginInfo(String name, String token) {
        this.name = name;
        this.token = token;
    }
}
