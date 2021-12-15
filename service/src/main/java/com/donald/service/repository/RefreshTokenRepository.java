package com.donald.service.repository;

import com.donald.service.model.RefreshToken;
import com.donald.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findById(Long refTokenId);

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}

