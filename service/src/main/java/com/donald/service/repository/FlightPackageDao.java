package com.donald.service.repository;

import com.donald.service.dto.FlightPackageIds;
import com.donald.service.model.Trip;

import java.util.List;

public interface FlightPackageDao {

    List<FlightPackageIds> findFlightPackageIdsOfATrip(Trip trip);
}
