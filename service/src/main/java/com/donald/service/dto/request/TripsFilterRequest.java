package com.donald.service.dto.request;

import com.donald.service.validator.EnumValidator;
import com.donald.service.model.enums.TripReason;
import com.donald.service.model.enums.TripStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class TripsFilterRequest {

    @Min(0)
    private int pageNo;
    @Min(1)
    private int pageSize;

    @EnumValidator(enumClass = TripReason.class, ignoreCase = true)
    private String reason;

    @EnumValidator(enumClass = TripStatus.class, ignoreCase = true)
    private String status;

}
