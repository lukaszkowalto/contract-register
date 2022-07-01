package controlling.contractRegister.web;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@ToString
public class FinancialDistributionMatrixDTO {

    private Integer id;

    @NotNull
    @Min(2021)
    @Max(2100)
    private double year;

    @NotNull
    @Min(1)
    @Max(12)
    private double month;

    @NotNull
    private Integer categoryId;

    @NotNull
    private Integer projectId;

    @NotNull
    @Min(0)
    @Max(999999999)
    private double amount;

    @NotNull
    @Min(0)
    @Max(999999999)
    private double amountVAT;
}