package com.donald.service.service.impl;


import com.donald.service.dto.FlightPackageIds;
import com.donald.service.dto.entity.UserDetailsImpl;
import com.donald.service.exception.BadRequestException;
import com.donald.service.exception.ConflictException;
import com.donald.service.exception.EntityNotFoundException;
import com.donald.service.exception.SqlDbException;
import com.donald.service.mapper.FlightMapper;
import com.donald.service.model.User;
import com.donald.service.repository.FlightPackageDao;
import com.donald.service.repository.FlightRepository;
import com.donald.service.repository.TripRepository;
import com.donald.service.dto.entity.FlightDto;
import com.donald.service.model.Flight;
import com.donald.service.model.Trip;
import com.donald.service.repository.UserRepository;
import com.donald.service.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.donald.service.model.enums.TripStatus.APPROVED;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final FlightPackageDao flightPackageDao;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final FlightMapper flightMapper;
    private static final Logger logger = LogManager.getLogger("com.donald.service.service");

    @Override
    public List<List<FlightDto>> getAllFlightPackagesByTripId(Long tripId) {
        User loggedInUser = getLoggedInUser();
        Trip trip = getTripByIdAndUser(tripId, loggedInUser);
        checkIfTripHasApprovedStatus(trip);

        List<FlightPackageIds> flightPackageIdsList = flightPackageDao.findFlightPackageIdsOfATrip(trip);
        List<List<FlightDto>> flightDtoPackages = processIntoFlightDtoPackages(flightPackageIdsList);
        return flightDtoPackages;
    }


    private List<List<FlightDto>> processIntoFlightDtoPackages(List<FlightPackageIds> flightPackageIdsList) {

        List<List<FlightDto>> flightDtoPackagesList = new ArrayList<>();

        flightPackageIdsList.forEach(item -> {
            try {
                Long[] flightIds = (Long[]) item.getFlights().getArray();
                List<Flight> flightPackage = flightRepository.findByIdIn(Arrays.asList(flightIds));

                if (flightPackage.size() == flightIds.length) {
                    List<FlightDto> flightDtoPackage = flightPackage
                            .stream()
                            .map(flight -> flightMapper.flightToFlightDto(flight))
                            .collect(Collectors.toList());
                    flightDtoPackagesList.add(flightDtoPackage);
                }
            } catch (SQLException e) {
                logger.error("Conversion error from java.sql type to java.lang");
                throw new SqlDbException("An error happened while converting data from the database!");
            }
        });
        return flightDtoPackagesList;
    }

    private void checkIfTripHasApprovedStatus(Trip trip) {
        if (!trip.getStatus().equals(APPROVED)) {
            logger.error(String.format("Can not have access to Flight Packages for this trip, because Trip status is not: %s.", APPROVED));
            throw new ConflictException(String.format("Trip status is not: %s. Can not get the flight packages for this trip",  APPROVED));
        }
    }

    private User getLoggedInUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId()).orElseThrow(
                () ->  new EntityNotFoundException("User not found!"));
    }
    private Trip getTripByIdAndUser(Long tripId, User loggedInUser) {
        return tripRepository.findByIdAndUser(tripId, loggedInUser).orElseThrow(
                () -> {
                    logger.error(String.format("Could find flights for the trip with id %s because it does not exist!", tripId ));
                    throw new BadRequestException(String.format("Provide a correct Trip ID. Trip with  id %s does not exist", tripId));
                }
        );
    }
}