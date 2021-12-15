package com.donald.controller.controller;

import com.donald.service.dto.entity.UserDto;
import com.donald.service.dto.request.LoginRequest;
import com.donald.service.dto.request.RefreshTokenRequest;
import com.donald.service.dto.request.UserRegisterRequest;
import com.donald.service.dto.response.GenericResponseMessage;
import com.donald.service.dto.response.JwtResponse;
import com.donald.service.dto.response.RefreshTokenResponse;
import com.donald.service.service.AuthenticationService;
import com.donald.service.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthenController {

    private final AuthenticationService authenticationService;
    private final UserService userService;



    @ApiOperation(value = "User login")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Successfully signed in"),
            @ApiResponse(code = 401, message = "Bad credentials - Unathorized")
    })
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authenticationService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    // client provides the refresh token, and gets back the new Access Token && the same Refresh Token
    @ApiOperation(value = "Provide refresh token, and get new access token.")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Successfully got the new Access JWT Token"),
            @ApiResponse(code = 403, message = "Refresh token not found in DB - Forbidden")
    })
    @PostMapping("/refreshToken")
    public ResponseEntity<RefreshTokenResponse> getRefreshedAccessToken(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse tokenRefreshResponse = authenticationService.getRefreshedAccessToken(request);
        return ResponseEntity.ok(tokenRefreshResponse);
    }

    @ApiOperation(value = "Register a new user")
    @ApiResponses( value ={
            @ApiResponse(code = 201, message = "Successfully created new user with the specified Uname, Pass & Roles"),
            @ApiResponse(code = 409, message = "Username is already taken")
    })
    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserRegisterRequest registerRequest) {
        UserDto createdUser = userService.registerNewUser(registerRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Logout of the app - expires refresh token")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/logout")
    public ResponseEntity<GenericResponseMessage> logout(@Valid @RequestBody RefreshTokenRequest logoutRequest) {
        GenericResponseMessage message = authenticationService.expireRefreshToken(logoutRequest);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
