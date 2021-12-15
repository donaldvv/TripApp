package com.donald.service.service.impl;
import com.donald.service.service.BookingService;
import com.donald.service.dto.entity.FlightDto;
import com.donald.service.dto.entity.TripDto;
import com.donald.service.dto.entity.UserDetailsImpl;
import com.donald.service.dto.page.PagedResponse;
import com.donald.service.dto.request.BookingRequest;
import com.donald.service.dto.response.BookingResponse;
import com.donald.service.dto.response.GenericResponseMessage;
import com.donald.service.model.Booking;
import com.donald.service.model.Flight;
import com.donald.service.model.Trip;
import com.donald.service.model.User;
import com.donald.service.exception.BadRequestException;
import com.donald.service.exception.ConflictException;
import com.donald.service.exception.EntityNotFoundException;
import com.donald.service.mapper.FlightMapper;
import com.donald.service.mapper.TripMapper;
import com.donald.service.repository.BookingRepository;
import com.donald.service.repository.FlightRepository;
import com.donald.service.repository.TripRepository;
import com.donald.service.repository.UserRepository;
import com.donald.service.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import static com.donald.service.model.enums.TripStatus.*;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final TripRepository tripRepository;
    private final FlightService flightService;
    private final FlightMapper flightMapper;
    private final TripMapper tripMapper;
    private final UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger("com.donald.service.service");


    @Override
    @Transactional
    public BookingResponse bookFlightPackageForATrip(Long tripId, BookingRequest bookingRequest) {
        User loggedInUser = getLoggedInUser();
        Trip trip = getTripOrThrow(tripId, loggedInUser);
        List<Long> bookingRequestIds = bookingRequest.getFlightIdsToBeBooked();

        checkIfTripHasApprovedStatus(trip);

        checkIfTripHasAlreadyBookedAPackage(trip);

        List<Flight> flightPackageRequest = flightRepository.findByIdIn(bookingRequestIds);
        validateBookingRequest(flightPackageRequest, bookingRequestIds, tripId);

        bookFlights(flightPackageRequest, trip);

        return processBookingResponse(trip);
    }


    @Override
    public BookingResponse getBookedFlightPackageByTripId(Long tripId) {
        User loggedInUser = getLoggedInUser();
        Trip trip = getTripOrThrow(tripId, loggedInUser);
        checkIfTripHasApprovedStatus(trip);

        List<Booking> bookings = bookingRepository.findAllByTrip(trip);
        List<FlightDto> flightDtos = bookings
                .stream()
                .map(booking -> booking.getFlight())
                .map(flight -> flightMapper.flightToFlightDto(flight))
                .collect(Collectors.toList());
        TripDto tripDto = tripMapper.tripToTripDto(trip);

        return new BookingResponse(tripDto, flightDtos);
    }

    @Override
    public PagedResponse<BookingResponse> getAllBookedFlightPackagesByUser(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);

        User loggedInUser = getLoggedInUser();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        // getting trips with APPROVED status only, bcs only them can have booked flights (otherwise response would include trips with empty flights array)
        Page<Trip> trips = tripRepository.findAllByUserAndStatus(loggedInUser, APPROVED, paging);
        trips.forEach(trip -> {
            List<Booking> bookings = bookingRepository.findAllByTrip(trip);
            List<FlightDto> flightDtos = bookings
                    .stream()
                    .map(booking -> booking.getFlight())
                    .map(flight -> flightMapper.flightToFlightDto(flight))
                    .collect(Collectors.toList());
            TripDto tripDto = tripMapper.tripToTripDto(trip);
            if (!flightDtos.isEmpty())
                bookingResponses.add(new BookingResponse(tripDto, flightDtos));
        });

        return new PagedResponse<>(bookingResponses, trips.getSize(), trips.getTotalElements());
    }


    @Override
    @Transactional
    public GenericResponseMessage deleteBookedFlightPackageByTripId(Long tripId) {
        User loggedInUser = getLoggedInUser();
        Trip trip = getTripOrThrow(tripId, loggedInUser);
        checkIfTripHasApprovedStatus(trip);

        Long bookingsInDb = bookingRepository.countByTrip(trip);
        if (bookingsInDb.intValue() == 0) {
            logger.error("The trip did not have any bookings.");
            throw new BadRequestException("This trip does not have any bookings(It does not have a flight package booked)");
        } else {
            bookingRepository.deleteByTrip(trip);
            return new GenericResponseMessage(String.format("Successfully deleted the flight package for trip with id %s", tripId));
        }
    }

    private Trip getTripOrThrow(Long tripId, User loggedInUser) {
        return tripRepository.findByIdAndUser(tripId, loggedInUser).orElseThrow(
                () -> {
                    logger.error(String.format("Trip with id %s was not found, so could not book the flight package", tripId));
                    throw new BadRequestException("Provide a correct Trip id");
                }
        );
    }

    private void checkIfTripHasApprovedStatus(Trip trip) {
        if (!trip.getStatus().equals(APPROVED)  && logger.isErrorEnabled()) {
            logger.error(String.format("Trip status is not: %s. Can not book a flight package if Trip is not %s", APPROVED, APPROVED));
            throw new ConflictException(String.format("Trip status is not: %s. Trip can not have booked flight packages if status is not %s", APPROVED, APPROVED));
        }
    }

    private void checkIfTripHasAlreadyBookedAPackage(Trip trip) {
        boolean exists = bookingRepository.existsByTrip(trip);
        if (exists) {
            logger.error("User has already booked a flight package for this trip!");
            throw new ConflictException("A flight package has already been booked for this trip!");
        }
    }

    private void validateBookingRequest(List<Flight> flightPackageRequest, List<Long> bookingRequestIds, Long tripId) {
        if (flightPackageRequest.size() == bookingRequestIds.size()) {
            List<List<FlightDto>> flightPackageInDb = flightService.getAllFlightPackagesByTripId(tripId);
            boolean valid = false;
            Collections.sort(bookingRequestIds);
            for (var flightPackage : flightPackageInDb) {
                List<Long> validIds = flightPackage
                        .stream()
                        .map(flightDto -> flightDto.getId())
                        .sorted()
                        .collect(Collectors.toList());

                if (bookingRequestIds.equals(validIds))
                    valid = true;
            }
            if (!valid) {
                logger.error("The ids in the bookingRequest, do not match with any of the flight packages available for the trip");
                throw new BadRequestException(String.format("Available Flights have been updated/deleted. Flight Package ids provided: %s , " +
                        "do not currently form a valid flight package for the trip: %s.",bookingRequestIds, tripId ));
            }
        }else {
            logger.error("Booking request, contains a Flight Id which is not valid (not in the database).");
            throw new BadRequestException(String.format("Can not book flight package! The booking request's flight Ids: %s, are not " +
                    "complete and can not form a valid flight package for the tripId %s", bookingRequestIds, tripId));
        }
    }

    private void bookFlights(List<Flight> flightPackageRequest, Trip trip) {
        flightPackageRequest.forEach(
                flight -> bookingRepository.save(new Booking(trip, flight))
        );
    }

    private BookingResponse processBookingResponse(Trip trip) {
        List<Booking> bookedFlightPackage = bookingRepository.findAllByTrip(trip);
        List<FlightDto> flightPackageResponse = bookedFlightPackage
                .stream()
                .map(booking -> flightMapper.flightToFlightDto(booking.getFlight()))
                .collect(Collectors.toList());
        TripDto tripDto = tripMapper.tripToTripDto(trip);

        return new BookingResponse(tripDto, flightPackageResponse);
    }

    private User getLoggedInUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId()).orElseThrow(
                () ->  new EntityNotFoundException("User not found!"));
    }
}
