package com.donald.service.dto.response;

import com.donald.service.dto.entity.UserDto;
import com.donald.service.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

// Used for the admin page only - Send trip owner also

@Getter
@Setter
public class TripWithUserDto {

    private Long id;

    public String reason;

    private String description;

    private String fromCity;

    private String toCity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime departureDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime arrivalDate;

    private String status;

    @JsonInclude
    private UserDto user;


}
