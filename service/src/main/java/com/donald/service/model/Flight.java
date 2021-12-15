package com.donald.service.model;
import com.donald.service.validator.TimeSpan;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;



@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="flights")
@TimeSpan(startTime = "departureDate", endTime = "arrivalDate")
public class Flight {

    @Id
    @SequenceGenerator(
            name = "flight_sequence",
            sequenceName = "flight_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "flight_sequence"
    )
    private Long id;

    @Column(name = "from_city", nullable = false)
    @NotBlank
    private String fromCity;

    @Column(name = "to_city", nullable = false)
    @NotBlank
    private String toCity;

    @Column(name = "departure_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime departureDate;

    @Column(name = "arrival_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime arrivalDate;

    @Column(name="price", nullable = false)
    private Double price;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Booking> bookings;


    public Flight(String fromCity, String toCity, LocalDateTime departureDate, LocalDateTime arrivalDate, Double price) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.price = price;
    }
}
