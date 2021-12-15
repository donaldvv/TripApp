package com.donald.service.service;

import com.donald.service.dto.request.RefreshTokenRequest;
import com.donald.service.dto.response.GenericResponseMessage;
import com.donald.service.dto.response.JwtResponse;
import com.donald.service.dto.response.RefreshTokenResponse;
import com.donald.service.dto.request.LoginRequest;

public interface AuthenticationService {

    JwtResponse authenticateUser(LoginRequest loginRequest);

    RefreshTokenResponse getRefreshedAccessToken(RefreshTokenRequest request);

    GenericResponseMessage expireRefreshToken(RefreshTokenRequest request);
}
