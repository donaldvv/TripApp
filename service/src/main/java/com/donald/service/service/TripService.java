package com.donald.service.service;

import com.donald.service.dto.entity.TripDto;
import com.donald.service.dto.request.TripCreateRequest;
import com.donald.service.dto.page.PagedResponse;
import com.donald.service.dto.request.TripsFilterRequest;
import com.donald.service.dto.response.TripWithUserDto;

public interface TripService {

    PagedResponse<TripDto> getTripsByUser(TripsFilterRequest request);

    TripDto createNewTrip(TripCreateRequest tripCreateRequest);

    TripDto getTripById(Long tripId);

    TripDto requestApproval(Long tripId);

    TripDto updateTrip(Long tripId, TripCreateRequest updateRequest);

    PagedResponse<TripWithUserDto> getTripsWaitingApproval(Integer pageNo, Integer pageSize);

    TripWithUserDto approveTrip(Long tripId);

}
