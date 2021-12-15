package com.donald.service.repository;

import com.donald.service.model.Trip;
import com.donald.service.model.User;
import com.donald.service.model.enums.TripReason;
import com.donald.service.model.enums.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    Page<Trip> findAllByUser(User user, Pageable pageable);

    Page<Trip> findAllByUserAndStatusAndReason(User user, TripStatus status, TripReason reason, Pageable pageable);
    Page<Trip> findAllByUserAndStatus(User user, TripStatus status, Pageable pageable);
    Page<Trip> findAllByUserAndReason(User user, TripReason reason, Pageable pageable);

    @Query("SELECT t FROM Trip t WHERE t.user = :user AND t.departureDate < :endTime AND :startTime < t.arrivalDate ")
    List<Trip> findTripsOfUserByDatesOverlap(User user, LocalDateTime startTime, LocalDateTime endTime);

    Optional<Trip> findByIdAndUser(Long tripId, User user);

    Page<Trip> findAllByStatus(TripStatus status, Pageable pageable);
}
