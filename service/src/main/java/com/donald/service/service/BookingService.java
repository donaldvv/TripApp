package com.donald.service.service;

import com.donald.service.dto.page.PagedResponse;
import com.donald.service.dto.request.BookingRequest;
import com.donald.service.dto.response.BookingResponse;
import com.donald.service.dto.response.GenericResponseMessage;

public interface BookingService {

    BookingResponse bookFlightPackageForATrip(Long tripId, BookingRequest bookingRequest);

    BookingResponse getBookedFlightPackageByTripId(Long tripId);

    PagedResponse<BookingResponse> getAllBookedFlightPackagesByUser(Integer pageNo, Integer pageSize);

    GenericResponseMessage deleteBookedFlightPackageByTripId(Long tripId);
}
