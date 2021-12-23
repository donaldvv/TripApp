package com.donald.service.service.impl;

import com.donald.service.dto.entity.TripDto;
import com.donald.service.dto.entity.UserDto;
import com.donald.service.dto.request.TripCreateRequest;
import com.donald.service.dto.entity.UserDetailsImpl;
import com.donald.service.dto.page.PagedResponse;
import com.donald.service.dto.request.TripsFilterRequest;
import com.donald.service.dto.response.TripWithUserDto;
import com.donald.service.exception.BadRequestException;
import com.donald.service.exception.ConflictException;
import com.donald.service.exception.EntityNotFoundException;
import com.donald.service.mapper.PageMapper;
import com.donald.service.mapper.TripMapper;
import com.donald.service.mapper.UserMapper;
import com.donald.service.repository.BookingRepository;
import com.donald.service.repository.TripRepository;
import com.donald.service.repository.UserRepository;
import com.donald.service.service.BookingService;
import com.donald.service.service.TripService;
import com.donald.service.model.Trip;
import com.donald.service.model.User;
import com.donald.service.model.enums.TripReason;
import com.donald.service.model.enums.TripStatus;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripMapper tripMapper;
    private final UserMapper userMapper;
    private final PageMapper pageMapper;
    private static final Logger logger = LogManager.getLogger("com.donald.service.service");



    @Override
    public PagedResponse<TripDto> getTripsByUser(TripsFilterRequest request) {
        Pageable paging = pageMapper.toPageable(request);
        User loggedInUser = getLoggedInUser();

        return getTripsBasedOnStatusAndReason(loggedInUser, request.getStatus(), request.getReason(), paging);
    }


    @Override
    @Transactional
    public TripDto createNewTrip(TripCreateRequest tripCreateRequest) {
        LocalDateTime departureDate = tripCreateRequest.getDepartureDate();
        LocalDateTime arrivalDate = tripCreateRequest.getArrivalDate();
        User user = getLoggedInUser();

        List<Trip> overlapingTrips = tripRepository.findTripsOfUserByDatesOverlap(user, departureDate, arrivalDate);
        if (overlapingTrips.isEmpty()) {
            Trip tripToBeAdded = tripMapper.tripCreateToTrip(tripCreateRequest);
            setReasonEnumToTrip(tripToBeAdded, tripCreateRequest);
            tripToBeAdded.setUser(user);
            Trip savedTrip = tripRepository.save(tripToBeAdded);
            return tripMapper.tripToTripDto(savedTrip);
        }
        else {
            logger.error("There are overlapping trips with the requested trip.");
            throw new ConflictException(String.format("The Time Span that this trip will last is overlaping with these " +
                    "trips that user has created: %s", overlapingTrips));
        }
    }

    @Override
    public TripDto getTripById(Long tripId) {
        User loggedInUser = getLoggedInUser();
        Trip trip = getTripEntityByIdAndUser(tripId, loggedInUser);
        return tripMapper.tripToTripDto(trip);
    }

    @Override
    @Transactional
    public TripDto requestApproval(Long tripId) {
        User user = getLoggedInUser();
        Trip tripToUpdate = getTripEntityByIdAndUser(tripId, user);
        verifyTripStatusCreated(tripToUpdate);
        tripToUpdate.setStatus(TripStatus.WAITING_FOR_APPROVAL);
        Trip updatedTrip = tripRepository.save(tripToUpdate);
        return tripMapper.tripToTripDto(updatedTrip);
    }

    @Override
    @Transactional
    public TripDto updateTrip(Long tripId, TripCreateRequest updateRequest) {
        LocalDateTime departureDate = updateRequest.getDepartureDate();
        LocalDateTime arrivalDate = updateRequest.getArrivalDate();
        User user = getLoggedInUser();
        Trip oldTrip = getTripEntityByIdAndUser(tripId, user);
        verifyTripNonApproved(oldTrip);

        List<Trip> overlapingTrips = tripRepository.findTripsOfUserByDatesOverlap(user, departureDate, arrivalDate);
        if (overlapingTrips.isEmpty() || (overlapingTrips.size()==1 && overlapingTrips.get(0).getId().equals(tripId))) {
            Trip tripToSave = tripMapper.tripCreateToTrip(updateRequest);
            tripToSave.setId(oldTrip.getId());
            setReasonEnumToTrip(tripToSave, updateRequest);
            tripToSave.setUser(oldTrip.getUser());
            Trip newTrip = tripRepository.save(tripToSave);
            return tripMapper.tripToTripDto(newTrip);
        }
        else {
            logger.error("There are overlapping trips with the requested trip.");
            throw new ConflictException(String.format("The Time Span that this trip will last is overlaping with these " +
                    "trips that user has created: %s", overlapingTrips));
        }
    }

    @Override
    public PagedResponse<TripWithUserDto> getTripsWaitingApproval(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Trip> trips = tripRepository.findAllByStatus(TripStatus.WAITING_FOR_APPROVAL, paging);
        List<Trip> tripsContent = trips.getContent();
        List<TripWithUserDto> tripsResponse = tripMapper.tripsToTripWithUserDtos(trips.getContent());
        for (var i=0; i < tripsResponse.size(); i++) {
            UserDto userDto = userMapper.userToUserDto(tripsContent.get(i).getUser());
            tripsResponse.get(i).setUser(userDto);
        }
        return new PagedResponse<>(tripsResponse, trips.getSize(), trips.getTotalElements());
    }

    @Override
    @Transactional
    public TripWithUserDto approveTrip(Long tripId) {
        Trip tripToUpdate = tripRepository.findById(tripId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Trip with id %s was not found", tripId)));
        verifyTripStatusWaitingApproval(tripToUpdate);
        tripToUpdate.setStatus(TripStatus.APPROVED);
        Trip updatedTrip = tripRepository.save(tripToUpdate);
        UserDto userDto = userMapper.userToUserDto(updatedTrip.getUser());
        TripWithUserDto response = tripMapper.tripToTripWithUserDto(updatedTrip);
        response.setUser(userDto);
        return response;
    }


    private User getLoggedInUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId()).get();
    }

    private PagedResponse<TripDto> getTripsBasedOnStatusAndReason(User loggedInUser, String status,
                                                                  String reason, Pageable paging) {
        Page<Trip> tripsPage;
        if (reason != null && status != null) {
            tripsPage = tripRepository.findAllByUserAndStatusAndReason(loggedInUser, getStatusEnum(status), getReasonEnum(reason), paging);
            List<TripDto> trips = mapTripsIntoTripDtos(tripsPage);
            return new PagedResponse<>(trips, tripsPage.getSize(), tripsPage.getTotalElements());
        } else if (reason != null) {
            tripsPage = tripRepository.findAllByUserAndReason(loggedInUser, getReasonEnum(reason), paging);
            List<TripDto> trips = mapTripsIntoTripDtos(tripsPage);
            return new PagedResponse<>(trips, tripsPage.getSize(), tripsPage.getTotalElements());
        } else if(status != null) {
            tripsPage = tripRepository.findAllByUserAndStatus(loggedInUser, getStatusEnum(status), paging);
            List<TripDto> trips = mapTripsIntoTripDtos(tripsPage);
            return new PagedResponse<>(trips, tripsPage.getSize(), tripsPage.getTotalElements());
        } else {
            tripsPage = tripRepository.findAllByUser(loggedInUser, paging);
            List<TripDto> trips = mapTripsIntoTripDtos(tripsPage);
            return new PagedResponse<>(trips, tripsPage.getSize(), tripsPage.getTotalElements());
        }
    }
    private List<TripDto> mapTripsIntoTripDtos(Page<Trip> tripsPage) {
        return tripsPage.getContent()
                .stream()
                .map(trip -> tripMapper.tripToTripDto(trip))
                .collect(Collectors.toList());
    }

    private void setReasonEnumToTrip(Trip tripToBeAdded,TripCreateRequest tripCreateRequest) {
        String reasonStr = tripCreateRequest.getReason();
        TripReason[] enumValues = TripReason.values();
        for (var enumVal : enumValues) {
            if(reasonStr.equalsIgnoreCase(enumVal.name())) {
                tripToBeAdded.setReason(enumVal);
                break;
            }
        }
    }

    // it will return a valid TripReason, bcs the request was validated (had custom EnumValidator)
    private TripReason getReasonEnum(String reasonStr) {
        TripReason[] enumValues = TripReason.values();
        int i;
        for (i=0; i < enumValues.length; i++) {
            if(reasonStr.equalsIgnoreCase(enumValues[i].name()))
                break;
        }
        return enumValues[i];
    }
    private TripStatus getStatusEnum(String statusStr) {
        TripStatus[] enumValues = TripStatus.values();
        int i;
        for (i=0; i < enumValues.length; i++) {
            if(statusStr.equalsIgnoreCase(enumValues[i].name()))
                break;
        }
        return enumValues[i];
    }
    private Trip getTripEntityByIdAndUser(Long tripId, User loggedInUser) {
        return tripRepository.findByIdAndUser(tripId, loggedInUser).orElseThrow(() -> {
            logger.error("Trip was not found in the database (or the trip with that id, does not belong to that user)");
            throw new EntityNotFoundException(String.format("Trip with id: %s, was not found!", tripId));
        });
    }

    private void verifyTripNonApproved(Trip trip) {
        if (trip.getStatus().equals(TripStatus.APPROVED)) {
            logger.error("Trip had APPROVED status - cant be updated!");
            throw new BadRequestException("Trip had APPROVED status; can't be updated!");
        }
    }

    private void verifyTripStatusCreated(Trip trip) {
        if ( !trip.getStatus().equals(TripStatus.CREATED)) {
            logger.error(String.format("Trip status wasn't %s, so it can not be approved by admin!", TripStatus.CREATED));
            throw new BadRequestException(String.format("Trip status was NOT %s, so it can not be approved by admin! Status " +
                    "was: %s", TripStatus.CREATED, trip.getStatus()));
        }
    }

    private void verifyTripStatusWaitingApproval(Trip trip) {
        if ( !trip.getStatus().equals(TripStatus.WAITING_FOR_APPROVAL)) {
            logger.error(String.format("Trip status was %s, so it can not be approved by admin!", TripStatus.WAITING_FOR_APPROVAL));
            throw new BadRequestException(String.format("Trip status was %s, so it can not be approved by admin! Actuall status " +
                    "was: %s", TripStatus.WAITING_FOR_APPROVAL, trip.getStatus()));
        }
    }
}
