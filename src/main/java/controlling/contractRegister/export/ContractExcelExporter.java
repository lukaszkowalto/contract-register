package controlling.contractRegister.export;

import controlling.contractRegister.model.Contract;
import controlling.contractRegister.util.DateUtil;
import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class ContractExcelExporter implements ExcelExporter<Contract> {
    @Override
    public Class<Contract> getObjectClass() {
        return Contract.class;
    }

    @Override
    public String getSheetName() {
        return "contracts.excel.sheet.name";
    }

    @Override
    public List<String> getHeaderNames() {
        return List.of(
                "contracts.excel.sheet.header.id",
                "contracts.excel.sheet.header.document-date",
                "contracts.excel.sheet.header.contract-number",
                "contracts.excel.sheet.header.contract-type",
                "contracts.excel.sheet.header.contract-date-from",
                "contracts.excel.sheet.header.contract-date-to",
                "contracts.excel.sheet.header.customer",
                "contracts.excel.sheet.header.contract-details",
                "contracts.excel.sheet.header.currency",
                "contracts.excel.sheet.header.exchange-rate",
                "contracts.excel.sheet.header.amount",
                "contracts.excel.sheet.header.amount-VAT",
                "contracts.excel.sheet.header.remarks"
        );
    }

    @Override
    public List<Pair<Object, CellStyle>> getCells(XSSFWorkbook workbook, Contract contract) {
        CellStyle style = ExcelCellStyleUtil.getDefaultCellStyle(workbook);
        CellStyle dateStyle = ExcelCellStyleUtil.getDateCellStyle(workbook);

        return List.of(
                Pair.create(contract.getId(), style),
                Pair.create(contract.getDocumentDate(), dateStyle),
                Pair.create(contract.getContractNumber(), style),
                Pair.create(contract.getContractType().getName(), style),
                Pair.create(contract.getContractDateFrom(), dateStyle),
                Pair.create(getContractDateTo(contract), dateStyle),
                Pair.create(contract.getCustomer().getCustomerHeader(), style),
                Pair.create(contract.getContractDetails(), style),
                Pair.create(contract.getCurrency().getName(), style),
                Pair.create(contract.getExchangeRate(), style),
                Pair.create(contract.getAmount(), style),
                Pair.create(getAmountVAT(contract), style),
                Pair.create(getRemarks(contract), style)
        );
    }

    private String getRemarks(Contract contract) {
        return Optional.ofNullable(contract.getRemarks()).orElse("");
    }

    private Double getAmountVAT(Contract contract) {
        return Optional.ofNullable(contract.getAmountVAT()).orElse(0.0);
    }

    private Date getContractDateTo(Contract contract) {
        return Optional.ofNullable(contract.getContractDateTo()).orElse(DateUtil.toDate("31-12-2100"));
    }
}