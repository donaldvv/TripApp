package com.donald.service.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class JwtResponse {

    private final String tokenType = "Bearer";
    private String accessToken;
    private String refreshToken;

    private Long userId;
    private String username;
    private Collection<String> userRoles;

    public JwtResponse(String accessToken, String refreshToken, Long userId, String username, Collection<String> userRoles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        this.userRoles = userRoles;
    }
}
