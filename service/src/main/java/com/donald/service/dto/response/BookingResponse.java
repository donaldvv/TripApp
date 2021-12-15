package com.donald.service.dto.response;
import com.donald.service.dto.entity.FlightDto;
import com.donald.service.dto.entity.TripDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private TripDto trip;

    private List<FlightDto> flightPackage;
}
