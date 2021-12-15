package com.donald.service.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.sql.Array;
import java.util.Date;

// This class is used to get the ResultSet of the jdbc method, that gets the ids of all possible flight combinations (direct or transit flights)
// that can get User from his trip's from_city -> to_city, within the time window determined in the trip (and also without any
// time conflicts between individual flights)

@AllArgsConstructor
@Getter
@Setter
@ToString
public class FlightPackageIds {

    private Array flights;
    private Date departureDate;
    private Date arrivalDate;
    private Array stepoverCities;
}
