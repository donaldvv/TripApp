package com.donald.service.service;

import com.donald.service.model.RefreshToken;

public interface RefreshTokenService {

    RefreshToken getByToken(String token);

    void verifyRefreshTokenExpiration(RefreshToken refreshToken);

    RefreshToken createRefreshToken(Long userId);
}
