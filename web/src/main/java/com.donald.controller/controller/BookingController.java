package com.donald.controller.controller;

import com.donald.service.dto.page.PagedResponse;
import com.donald.service.dto.response.BookingResponse;
import com.donald.service.dto.response.GenericResponseMessage;
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

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;



    @ApiOperation(value = "Get all available flight packages for this trip (direct & transit flights) using logged in user context")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Successfully got the booked flight package for this trip")
    })
    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<BookingResponse> getAllBookedFlightPackagesByUser(@RequestParam(defaultValue = "0") @Min(0) Integer pageNo,
                                                                           @RequestParam(defaultValue = "3") @Min(1) Integer pageSize ) {
        return bookingService.getAllBookedFlightPackagesByUser(pageNo, pageSize);
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Get all available flight packages for this trip (direct & transit flights) using trip id")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Successfully got the booked flight package for this trip"),
            @ApiResponse(code = 400, message = "Provide a valid Trip ID"),
            @ApiResponse(code = 409, message = "Trip status is not APPROVED, so there are no flight booked for this trip.")
    })
    @GetMapping("/{tripId}")
    public ResponseEntity<BookingResponse> getBookedFlightPackageByTripId(@PathVariable @ApiParam(name = "tripId", required = true)
                                                                              @Min(1L) Long tripId) {
        BookingResponse bookingResponse = bookingService.getBookedFlightPackageByTripId(tripId);
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Delete the flight package for this trip (deletes all the flights for that trip)")
    @ApiResponses( value ={
            @ApiResponse(code = 200, message = "Successfully deleted"),
            @ApiResponse(code = 400, message = "Provide a valid Trip ID / Trip doesn't have a booked flight package"),
            @ApiResponse(code = 409, message = "Trip status is not APPROVED, there are no flights booked for this trip.")
    })
    @DeleteMapping("/{tripId}")
    public ResponseEntity<GenericResponseMessage> deleteBookedFlightPackageForTrip(@PathVariable @ApiParam(name = "tripId", required = true)
                                                                                       @Min(1L) Long tripId) {
        GenericResponseMessage responseSuccess = bookingService.deleteBookedFlightPackageByTripId(tripId);
        return ResponseEntity.ok(responseSuccess);
    }
}
