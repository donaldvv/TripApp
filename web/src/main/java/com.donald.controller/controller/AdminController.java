package com.donald.controller.controller;

import com.donald.service.service.UserService;
import com.donald.service.dto.entity.UserDto;
import com.donald.service.dto.page.PagedResponse;
import com.donald.service.dto.request.UserRegisterRequest;
import com.donald.service.dto.response.TripWithUserDto;
import com.donald.service.service.TripService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/admin")
@Validated
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final TripService tripService;


    @ApiOperation(value = "Get All Users")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Successfully got the list of All users (admin/user/both)")
    })
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<UserDto> getUsers(@RequestParam(defaultValue = "0") @Min(0) Integer pageNo,
                                           @RequestParam(defaultValue = "50") @Min(1) Integer pageSize ) {
        return userService.getAllUsers(pageNo, pageSize);
    }

    @ApiOperation(value = "Get User")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Successfully got the user"),
            @ApiResponse(code = 404, message = "User not found!")
    })
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable @Min(1) Long userId) {
        UserDto user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "Get trips WAITING_FOR_APPROVAL")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Successfully got the list of trips WAITING_FOR_APPROVAL")
    })
    @GetMapping("/tripsForApproval")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<TripWithUserDto> getTripsWaitingApproval(@RequestParam(defaultValue = "0") @Min(0) Integer pageNo,
                                                                  @RequestParam(defaultValue = "50") @Min(1) Integer pageSize) {
        return tripService.getTripsWaitingApproval(pageNo, pageSize);
    }

    @ApiOperation(value = "Approve a Trip")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Trip successfully Approved"),
            @ApiResponse(code = 404, message = "Trip was not found")
    })
    @PatchMapping("/tripsForApproval/{tripId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TripWithUserDto> approveTrip(@PathVariable @Min(1) Long tripId) {
        TripWithUserDto tripDto = tripService.approveTrip(tripId);
        return new ResponseEntity<>(tripDto, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponses( value ={
            @ApiResponse(code = 201, message = "Successfully updated user with the specified Uname, Pass & Roles"),
            @ApiResponse(code = 400, message = "Admin can not remove admin role from a user"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 409, message = "Username is already taken")
    })
    public ResponseEntity<UserDto> updateUser(@PathVariable @Min(1) Long userId,
                                              @Valid @RequestBody UserRegisterRequest updateRequest) {
        UserDto updatedUser = userService.updateUser(userId, updateRequest);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


}
