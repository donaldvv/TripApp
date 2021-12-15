package com.donald.service.model;
import com.donald.service.model.enums.TripReason;
import com.donald.service.model.enums.TripStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user","bookings"} )
@Table(name = "trips")
public class Trip {

    @Id
    @SequenceGenerator(
            name = "trip_sequence",
            sequenceName = "trip_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "trip_sequence"
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    public TripReason reason;

    @Column(nullable = false)
    @NotBlank
    private String description;

    @Column(name="from_city", nullable = false)
    @NotBlank
    private String fromCity;

    @Column(name="to_city", nullable = false)
    @NotBlank
    private String toCity;

    @Column(name="departure_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime departureDate;

    @Column(name="arrival_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime arrivalDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private TripStatus status = TripStatus.CREATED;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


    @OneToMany(mappedBy = "trip", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Booking> bookings;
}
