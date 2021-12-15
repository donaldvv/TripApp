package com.donald.service.dto.request;

import com.donald.service.validator.EnumValidator;
import com.donald.service.model.enums.TripReason;
import com.donald.service.validator.TimeSpan;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@TimeSpan(startTime = "departureDate", endTime = "arrivalDate")
public class TripCreateRequest {

    @NotBlank
    @EnumValidator(enumClass = TripReason.class, ignoreCase = true)
    @Size(min = 5, max = 8)
    private String reason;

    @NotBlank
    private String description;

    @NotBlank
    private String fromCity;

    @NotBlank
    private String toCity;

    @NotNull(message = "The departure date is required.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime departureDate;

    @NotNull(message = "The arrival date is required.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime arrivalDate;


}
