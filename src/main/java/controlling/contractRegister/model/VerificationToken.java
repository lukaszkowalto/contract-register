package controlling.contractRegister.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Table(name = "[verification_token]", schema = "dbo")
public class VerificationToken {

    @Id
    @Column(name = "customer_id")
    private Integer id;

    @Type(type = "uuid-char")
    @Column(columnDefinition = "uniqueidentifier", nullable = false, unique = true)
    private UUID token = UUID.randomUUID();

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, name = "expiry_date")
    private LocalDateTime expiryDate = LocalDateTime.now().plusDays(1);

    @CreationTimestamp
    @Column(name = "create_dttm", nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "update_dttm")
    private LocalDateTime updateDateTime;

    public boolean isTokenValid() {
        return expiryDate.isAfter(LocalDateTime.now());
    }
}
