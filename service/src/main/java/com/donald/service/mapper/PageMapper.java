package com.donald.service.mapper;

import com.donald.service.dto.request.TripsFilterRequest;
import org.springframework.data.domain.Pageable;

public interface PageMapper {
    // not generic, bcs i only used it for the endpoint where i need to get Trips and have the ability to filter them
    Pageable toPageable(TripsFilterRequest request);
}
