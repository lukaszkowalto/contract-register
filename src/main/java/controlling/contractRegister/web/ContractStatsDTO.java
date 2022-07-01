package controlling.contractRegister.web;

import controlling.contractRegister.util.DoubleUtil;
import controlling.contractRegister.util.IntegerUtil;
import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@ToString
public class ContractStatsDTO {

    private Integer numberOfContracts;

    private Integer prevNumberOfContracts;

    private double grossAmountOfContracts;

    private double prevGrossAmountOfContracts;

    private long numberOfContractsTrend;

    private long grossAmountOfContractsTrend;

    public ContractStatsDTO(Integer numberOfContracts, Integer prevNumberOfContracts, double grossAmountOfContracts, double prevGrossAmountOfContracts) {
        this.numberOfContracts = numberOfContracts;
        this.prevNumberOfContracts = prevNumberOfContracts;
        this.grossAmountOfContracts = grossAmountOfContracts;
        this.prevGrossAmountOfContracts = prevGrossAmountOfContracts;
        this.numberOfContractsTrend = IntegerUtil.getTrend(numberOfContracts, prevNumberOfContracts);
        this.grossAmountOfContractsTrend = DoubleUtil.getTrend(grossAmountOfContracts, prevGrossAmountOfContracts);
    }
}