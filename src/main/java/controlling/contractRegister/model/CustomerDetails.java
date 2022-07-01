package controlling.contractRegister.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Table(name = "[customer_details]", schema = "dbo")
public class CustomerDetails {

    @Id
    @Column(name = "customer_id")
    private Integer id;

    @Column(length = 64, nullable = true)
    private String email;

    @Column(name = "cell_phone", length = 32, nullable = true)
    private String cellPhone;

    @Column(length = 16, nullable = true)
    private String regon;

    @Column(length = 16, nullable = true)
    private String krs;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @MapsId
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @CreationTimestamp
    @Column(name = "create_dttm", nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "update_dttm")
    private LocalDateTime updateDateTime;
}