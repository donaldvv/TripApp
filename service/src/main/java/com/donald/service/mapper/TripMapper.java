package com.donald.service.mapper;

import com.donald.service.dto.entity.TripDto;
import com.donald.service.dto.request.TripCreateRequest;
import com.donald.service.dto.response.TripWithUserDto;
import com.donald.service.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TripMapper {

    @Mapping(target="reason", expression= "java(trip.getReason().name())")
    @Mapping(target="status", expression= "java(trip.getStatus().name())")
    TripDto tripToTripDto(Trip trip);

    @Mapping(target="reason", expression= "java(trip.getReason().name())")
    @Mapping(target="status", expression= "java(trip.getStatus().name())")
    @Mapping(target = "user", ignore = true)
    TripWithUserDto tripToTripWithUserDto(Trip trip);

    // trip id = null && reason = null
    @Mapping(target = "reason", ignore = true)
    Trip tripCreateToTrip(TripCreateRequest tripCreateRequest);


    List<TripWithUserDto> tripsToTripWithUserDtos(List<Trip> trips);
}
