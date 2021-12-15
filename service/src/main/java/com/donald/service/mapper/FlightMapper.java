package com.donald.service.mapper;

import com.donald.service.dto.entity.FlightDto;
import com.donald.service.model.Flight;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    FlightDto flightToFlightDto(Flight flight);
}
