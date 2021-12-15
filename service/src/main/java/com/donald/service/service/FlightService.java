package com.donald.service.service;

import com.donald.service.dto.entity.FlightDto;

import java.util.List;

public interface FlightService {

    List<List<FlightDto>> getAllFlightPackagesByTripId(Long tripId);
}
