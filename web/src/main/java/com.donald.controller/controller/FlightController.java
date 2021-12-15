package com.donald.controller.controller;

import com.donald.service.dto.entity.FlightDto;
import com.donald.service.dto.request.BookingRequest;
import com.donald.service.dto.response.BookingResponse;
import com.donald.service.service.BookingService;
import com.donald.service.service.FlightService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@Validated
public class FlightController {

    private final FlightService flightService;
    private final BookingService bookingService;



    @ApiOperation(value = "Get all available flight packages for this trip (direct & transit flights)")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Successfully got available flight packages"),
            @ApiResponse(code = 400, message = "Provide a valid Trip ID"),
            @ApiResponse(code = 409, message = "Trip status is not APPROVED, you can not check the flight packages")
    })
    @GetMapping("/{tripId}")
    @PreAuthorize("hasRole('USER')")
    public List<List<FlightDto>> getAvailableFlightPackagesByTripId(@PathVariable @ApiParam(name = "tripId", required = true) @Min(1L) Long tripId) {
        return flightService.getAllFlightPackagesByTripId(tripId);
    }


    @ApiOperation(value = "Book the chosen flight package for this trip")
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Successfully booked the flight package for this trip"),
            @ApiResponse(code = 400, message = "Trip id not correct / Flight Package Ids do not form a currently valid flight package / Flight Package Ids not complete"),
            @ApiResponse(code = 409, message = "A flight package has already been booked for this trip")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{tripId}")
    public ResponseEntity<BookingResponse> bookFlightPackageForATrip( @PathVariable @ApiParam(name = "tripId", required = true) @Min(1L) Long tripId,
                                                                      @RequestBody @Valid BookingRequest bookingRequest) {

        BookingResponse bookingResponse = bookingService.bookFlightPackageForATrip(tripId, bookingRequest);
        return new ResponseEntity<>(bookingResponse, HttpStatus.CREATED);
    }



}
