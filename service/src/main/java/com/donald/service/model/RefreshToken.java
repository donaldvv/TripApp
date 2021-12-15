package com.donald.service.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.Instant;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "refreshtokens")
public class RefreshToken {

    @Id
    @SequenceGenerator(
            name = "refreshtoken_sequence",
            sequenceName = "refreshtoken_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "refreshtoken_sequence"
    )
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Instant expiration;

    @Column(unique = true, nullable = false)
    private String token;

    public RefreshToken(User user, String token, Instant expiration) {
        this.user = user;
        this.expiration = expiration;
        this.token = token;
    }
}
