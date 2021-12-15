package com.donald.service.service.impl;
import com.donald.service.dto.entity.UserDto;
import com.donald.service.dto.request.RefreshTokenRequest;
import com.donald.service.dto.response.GenericResponseMessage;
import com.donald.service.dto.response.JwtResponse;
import com.donald.service.dto.response.RefreshTokenResponse;
import com.donald.service.service.AuthenticationService;
import com.donald.service.service.RefreshTokenService;
import com.donald.service.service.UserService;
import com.donald.service.util.JwtUtil;
import com.donald.service.dto.request.LoginRequest;
import com.donald.service.model.RefreshToken;
import com.donald.service.model.User;
import com.donald.service.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private static final Logger logger = LogManager.getLogger("com.donald.service.service");



    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        // manually set this user as authenticated
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String jwtToken = jwtUtil.generateTokenFromUserDetails(userDetails);

        Collection<String> userRoles = getRolesFromUserDetails(userDetails);
        UserDto user = userService.getUserByUsername(loginRequest.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new JwtResponse(
                jwtToken,
                refreshToken.getToken(),
                user.getId(),
                user.getUsername(),
                userRoles);
    }


    @Override
    public RefreshTokenResponse getRefreshedAccessToken(RefreshTokenRequest request) {
        String requestTokenRefresh = request.getRefreshToken();
        RefreshToken tokenInTheDb = refreshTokenService.getByToken(requestTokenRefresh);
        refreshTokenService.verifyRefreshTokenExpiration(tokenInTheDb);
        User user = tokenInTheDb.getUser();
        String jwtAccessToken = jwtUtil.generateTokenWithUsername(user.getUsername());

        return new RefreshTokenResponse(jwtAccessToken, requestTokenRefresh);
    }


    @Override
    public GenericResponseMessage expireRefreshToken(RefreshTokenRequest request) {
        String requestTokenRefresh = request.getRefreshToken();
        RefreshToken tokenInTheDb = refreshTokenService.getByToken(requestTokenRefresh);
        tokenInTheDb.setExpiration(Instant.now());
        refreshTokenRepository.save(tokenInTheDb);
        return new GenericResponseMessage("Successfully expired the refresh token!");
    }

    private Collection<String> getRolesFromUserDetails(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .map(grantedAuth -> grantedAuth.getAuthority())
                .collect(Collectors.toSet());
    }

}
