package com.donald.service.repository.jdbcdao;

import com.donald.service.model.Trip;
import com.donald.service.dto.FlightPackageIds;
import com.donald.service.exception.SqlDbException;
import com.donald.service.repository.FlightPackageDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class FlightPackageDaoImpl implements FlightPackageDao {

    private final Connection conn;

    @Override
    public List<FlightPackageIds> findFlightPackageIdsOfATrip(Trip trip) {

        String query =
                "WITH RECURSIVE flight_paths (from_city, flights, path, to_city, departure_date, arrival_date) " +
                        "AS ( " +
                        "SELECT " +
                        "  from_city " +
                        ", ARRAY[id]" +
                        ", ARRAY[from_city]::character varying[] " +
                        ", to_city " +
                        ", departure_date " +
                        ", arrival_date " +
                        "FROM flights " +
                        "UNION ALL " +
                        "SELECT " +
                        "  fp.from_city " +
                        ", fp.flights || (f.id ) " +
                        ", fp.path || f.from_city " +
                        ", f.to_city " +
                        ", fp.departure_date " +
                        ", f.arrival_date " +
                        "FROM flights f " +
                        "JOIN flight_paths fp ON f.from_city = fp.to_city " +
                        "WHERE NOT f.from_city = ANY(fp.path) " +
                        "  AND NOT f.to_city = ANY(fp.path) " +
                        "  AND f.departure_date > fp.arrival_date " +
                        ") " +
                        "SELECT flights , departure_date as departureDate, arrival_date as arrivalDate, path[2:] stepoverCities " +
                        "FROM flight_paths " +
                        "WHERE from_city = ? AND to_city = ?  and departure_date > ? and arrival_date < ? ;  ";
        PreparedStatement prepSt = null;
        List<FlightPackageIds> flightPackageIdsList = null;
        try {
            prepSt = conn.prepareStatement(query);
            prepSt.setString(1, trip.getFromCity());
            prepSt.setString(2, trip.getToCity());
            prepSt.setTimestamp(3, Timestamp.valueOf(trip.getDepartureDate()));
            prepSt.setTimestamp(4, Timestamp.valueOf(trip.getArrivalDate()));
            ResultSet rs = prepSt.executeQuery();

            flightPackageIdsList = new ArrayList<>();
            while (rs.next()) {
                flightPackageIdsList.add(new FlightPackageIds(rs.getArray(1), rs.getTimestamp(2), rs.getTimestamp(3), rs.getArray(4)));
            }
            return flightPackageIdsList;

        } catch (SQLException throwables) {
            throw new SqlDbException("A problem regarding the Database occurred in the server");
        } finally {
            if (prepSt != null)
                try {
                    prepSt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

    }
}