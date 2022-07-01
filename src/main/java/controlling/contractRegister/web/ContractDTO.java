package controlling.contractRegister.web;

import controlling.contractRegister.dic.ContractType;
import controlling.contractRegister.dic.Currency;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@ToString
public class ContractDTO {

    private Integer id;

    @NotNull
    @Size(min = 1, max = 32)
    private String contractNumber;

    @NotNull
    private ContractType contractType;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date documentDate;

    @NotNull
    private Integer customerId;

    @NotNull
    @Size(min = 1, max = 4000)
    private String contractDetails;

    @NotNull
    private Currency currency;

    @NotNull
    @Min(0)
    @Max(100)
    private double exchangeRate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date contractDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date contractDateTo;

    @NotNull
    @Min(0)
    @Max(999999999)
    private double amount;

    @Min(0)
    @Max(999999999)
    private double amountVAT;

    @Size(min = 0, max = 4000)
    private String remarks;

    private List<FinancialDistributionMatrixDTO> fdmDTOS;

    private List<ContractAttachmentDTO> contractAttachmentDTOS;
}