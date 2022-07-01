package controlling.contractRegister.model;

import controlling.contractRegister.dic.CustomerGender;
import controlling.contractRegister.dic.CustomerType;
import controlling.contractRegister.util.StringUtil;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Embeddable
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Table(name = "[customer]", schema = "dbo")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "employee_flg")
    private Boolean employeeFlg = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", length = 16, nullable = false)
    private CustomerType customerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 16, nullable = true)
    private CustomerGender customerGender;

    @Column(name = "first_name", length = 32, nullable = true)
    private String firstName;

    @Column(name = "last_name", length = 32, nullable = true)
    private String lastName;

    @Column(length = 64, nullable = true)
    private String name;

    @Column(length = 16, nullable = true)
    private String pesel;

    @Column(length = 32, nullable = true)
    private String nip;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private CustomerAddress customerAddress;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private CustomerDetails customerDetails;

    @CreationTimestamp
    @Column(name = "create_dttm", nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "update_dttm")
    private LocalDateTime updateDateTime;

    public String getCustomerHeader() {
        return StringUtil.replaceNullWithEmptyString(firstName)
                + " " + StringUtil.replaceNullWithEmptyString(lastName)
                + " " + StringUtil.replaceNullWithEmptyString(pesel)
                + " " + StringUtil.replaceNullWithEmptyString(name)
                + " " + StringUtil.replaceNullWithEmptyString(nip);
    }
}