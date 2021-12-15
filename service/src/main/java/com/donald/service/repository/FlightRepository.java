package com.donald.service.repository;

import com.donald.service.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {


    List<Flight> findByIdIn(List<Long> flightPackageIds);

    Optional<Flight> findById(Long id);

    List<Flight> findAll();


}
