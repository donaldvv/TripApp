package com.donald.service.repository;

import com.donald.service.model.Booking;
import com.donald.service.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByTrip(Trip trip);

    Boolean existsByTrip(Trip trip);

    Long countByTrip(Trip trip);

    Long deleteByTrip(Trip trip);
}
