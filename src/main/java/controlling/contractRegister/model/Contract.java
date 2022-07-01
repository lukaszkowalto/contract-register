package controlling.contractRegister.model;

import controlling.contractRegister.dic.ContractType;
import controlling.contractRegister.dic.Currency;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Table(name = "[contract]", schema = "dbo")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "contract_number", length = 32, nullable = false)
    private String contractNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", length = 32, nullable = false)
    private ContractType contractType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "document_date", nullable = false)
    private Date documentDate;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(name = "contract_details", nullable = false)
    private String contractDetails;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 4)
    private Currency currency;

    @Column(name = "exchange_rate", nullable = false, precision = 8, scale = 4)
    private double exchangeRate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "contract_date_from", nullable = false)
    private Date contractDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "contract_date_to")
    private Date contractDateTo;

    @Column(nullable = false, precision = 11, scale = 2)
    private double amount;

    @Column(name = "amount_vat", precision = 11, scale = 2)
    private double amountVAT;

    private String remarks;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinancialDistributionMatrix> financialDistributionMatrix;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractAttachment> contractAttachments;

    @CreationTimestamp
    @Column(name = "create_dttm", nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "update_dttm")
    private LocalDateTime updateDateTime;

    public void setContractAttachments(List<ContractAttachment> contractAttachments) {
        if (this.contractAttachments == null) {
            this.contractAttachments = contractAttachments;
        } else {
            this.contractAttachments.clear();
            this.contractAttachments.addAll(contractAttachments);
        }
    }
}
