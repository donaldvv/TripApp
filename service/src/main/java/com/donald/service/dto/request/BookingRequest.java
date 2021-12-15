package com.donald.service.dto.request;

import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class BookingRequest {

    @NotNull
    @NotEmpty
    private List<Long> flightIdsToBeBooked;
}
