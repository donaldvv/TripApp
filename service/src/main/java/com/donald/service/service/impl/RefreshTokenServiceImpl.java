package com.donald.service.service.impl;

import com.donald.service.repository.UserRepository;
import com.donald.service.service.RefreshTokenService;
import com.donald.service.util.JwtUtil;
import com.donald.service.model.RefreshToken;
import com.donald.service.model.User;
import com.donald.service.exception.EntityNotFoundException;
import com.donald.service.exception.TokenRefreshException;
import com.donald.service.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger("com.donald.service.service");


    @Override
    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    logger.error("Refresh Token not found in the Database");
                    throw new TokenRefreshException(token, "Refresh Token not found in the Database");
                });
    }

    @Override
    public void verifyRefreshTokenExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiration().compareTo(Instant.now()) < 0) {
            logger.error(String.format("Refresh Token: %s has expired", refreshToken.getToken()));
            refreshTokenRepository.delete(refreshToken);
            logger.debug("Expired Refresh Token was deleted from the Database");
            throw new TokenRefreshException( refreshToken.getToken(), "Refresh Token has expired");
        }
    }


    @Override
    public RefreshToken createRefreshToken(Long userId) {
        Long refreshTokenDuration = jwtUtil.getRefreshTokenDuration();
        User user = findUserOrThrow(userId);
        RefreshToken refreshTokenToSave = new RefreshToken(
                user,
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(refreshTokenDuration)
        );
        return refreshTokenRepository.save(refreshTokenToSave);
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error(String.format("User with id: %s does not exist. Could not create the refresh token!", userId));
                    return new EntityNotFoundException("User does not exist. Could not create the refresh token.");
                });
    }

}
