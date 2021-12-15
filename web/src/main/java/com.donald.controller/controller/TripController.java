package com.donald.controller.controller;

import com.donald.service.dto.entity.TripDto;
import com.donald.service.dto.request.TripCreateRequest;
import com.donald.service.dto.page.PagedResponse;
import com.donald.service.dto.request.TripsFilterRequest;
import com.donald.service.service.TripService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;


@RestController
@RequestMapping("/api/trips")
@Validated
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;


    @ApiOperation(value = "Get all trips of the signed-in user")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Successfully got all the trips of the user")
    })
    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<TripDto> getTripsByUser(@RequestBody @Valid TripsFilterRequest request) {
        return tripService.getTripsByUser(request);
    }


    @ApiOperation(value = "Create a new trip for the signed-in user")
    @ApiResponses( value ={
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 409, message = "User has other trips happening between the departure " +
                    "and arrival date of the requested trip")
    })
    @PostMapping("/createTrip")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TripDto> createNewTrip(@RequestBody(required = true) @Valid TripCreateRequest tripCreateRequest) {
        TripDto createdTrip = tripService.createNewTrip(tripCreateRequest);
        return new ResponseEntity<>(createdTrip, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get one of logged-in user's Trips using tripId")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Got trip successfully"),
            @ApiResponse(code = 404, message = "Trip was not found")
    })
    @GetMapping("/{tripId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TripDto> getTripById(@PathVariable @Min(1) Long tripId) {
        TripDto tripDto = tripService.getTripById(tripId);
        return new ResponseEntity<>(tripDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Request approval for a trip")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Approval request sent to admin. Status = WAITING_FOR_APPROVAL"),
            @ApiResponse(code = 404, message = "Trip was not found")
    })
    @PatchMapping("/{tripId}/requestApproval")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TripDto> requestTripApproval(@PathVariable @Min(1) Long tripId) {
        TripDto tripDto = tripService.requestApproval(tripId);
        return new ResponseEntity<>(tripDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Successfully Updated a non-approved trip")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Updated trip successfully"),
            @ApiResponse(code = 404, message = "Trip was not found"),
            @ApiResponse(code = 400, message = "Trip status was APPROVED - can not update!"),
            @ApiResponse(code = 409, message = "User has other trips happening between the departure " +
                    "and arrival date of the requested trip")
    })
    @PutMapping("/{tripId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TripDto> updateTrip(@PathVariable @Min(1) Long tripId, @RequestBody @Valid TripCreateRequest updateRequest) {
        TripDto tripDto = tripService.updateTrip(tripId, updateRequest);
        return new ResponseEntity<>(tripDto, HttpStatus.OK);
    }



}
