package com.donald.service;

import com.donald.service.dto.entity.UserDetailsImpl;
import com.donald.service.dto.entity.UserDto;
import com.donald.service.dto.request.LoginRequest;
import com.donald.service.dto.request.RefreshTokenRequest;
import com.donald.service.dto.response.GenericResponseMessage;
import com.donald.service.dto.response.JwtResponse;
import com.donald.service.dto.response.RefreshTokenResponse;
import com.donald.service.mapper.UserMapper;
import com.donald.service.model.RefreshToken;
import com.donald.service.model.User;
import com.donald.service.repository.RefreshTokenRepository;
import com.donald.service.repository.RoleRepository;
import com.donald.service.repository.UserRepository;
import com.donald.service.service.RefreshTokenService;
import com.donald.service.service.UserService;
import com.donald.service.service.impl.AuthenticationServiceImpl;
import com.donald.service.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;



@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestAuthenticationServiceImpl {

    @Mock
    private RefreshTokenService refreshTokenService;


    @Mock
    private JwtUtil jwtUtils;


    @InjectMocks
    private AuthenticationServiceImpl authenticationService;


    @DisplayName("Send user the new access token and the same refresh token")
    @Test
    void getRefreshedAccessToken() {
        RefreshTokenRequest request = Mockito.mock(RefreshTokenRequest.class);
        User user = Mockito.mock(User.class);
        RefreshToken refreshToken = new RefreshToken(1L, user,  Instant.now().plusMillis(1235122312), "146523671231223");
        int jwtExpirationMs = 3600000;
        String jwtSecret = "lufthansaInternshipJava2021";
        Map<String, Object> claims = new HashMap<>();
        String jwtToken = Jwts.builder()
                .setSubject("donald")
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+ jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();

        Mockito.when(request.getRefreshToken()).thenReturn("146523671231223");
        Mockito.when(refreshTokenService.getByToken("146523671231223")).thenReturn(refreshToken);
        Mockito.when(jwtUtils.generateTokenWithUsername("donald")).thenReturn(jwtToken);
        Mockito.when(user.getUsername()).thenReturn("donald");
        RefreshTokenResponse response = authenticationService.getRefreshedAccessToken(request);

        assertEquals(jwtToken, response.getAccessToken());
        assertEquals("146523671231223", response.getRefreshToken());
    }


}
