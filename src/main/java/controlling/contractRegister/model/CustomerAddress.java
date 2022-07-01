package controlling.contractRegister.model;

import controlling.contractRegister.dic.CustomerAddressCountry;
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
@Table(name = "[customer_address]", schema = "dbo")
public class CustomerAddress {

    @Id
    @Column(name = "customer_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 64, nullable = false)
    private CustomerAddressCountry country;

    @Column(length = 256, nullable = false)
    private String place;

    @Column(length = 256, nullable = true)
    private String street;

    @Column(name = "house_number", length = 16, nullable = false)
    private String houseNumber;

    @Column(name = "flat_number", length = 16, nullable = true)
    private String flatNumber;

    @Column(name = "zip_code", length = 32, nullable = true)
    private String zipCode;

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