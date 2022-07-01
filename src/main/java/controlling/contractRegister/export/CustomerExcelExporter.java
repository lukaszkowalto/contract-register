package controlling.contractRegister.export;

import controlling.contractRegister.dic.CustomerGender;
import controlling.contractRegister.model.Customer;
import controlling.contractRegister.model.CustomerDetails;
import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomerExcelExporter implements ExcelExporter<Customer> {

    @Override
    public Class<Customer> getObjectClass() {
        return Customer.class;
    }

    @Override
    public String getSheetName() {
        return "customers.excel.sheet.name";
    }

    @Override
    public List<String> getHeaderNames() {
        return List.of(
                "customers.excel.sheet.header.id",
                "customers.excel.sheet.header.type",
                "customers.excel.sheet.header.first-name",
                "customers.excel.sheet.header.last-name",
                "customers.excel.sheet.header.pesel",
                "customers.excel.sheet.header.gender",
                "customers.excel.sheet.header.employee-flag",
                "customers.excel.sheet.header.name",
                "customers.excel.sheet.header.nip",
                "customers.excel.sheet.header.country",
                "customers.excel.sheet.header.place",
                "customers.excel.sheet.header.street",
                "customers.excel.sheet.header.house-number",
                "customers.excel.sheet.header.flat-number",
                "customers.excel.sheet.header.zip-code",
                "customers.excel.sheet.header.email",
                "customers.excel.sheet.header.cell-phone",
                "customers.excel.sheet.header.regon",
                "customers.excel.sheet.header.krs"
        );
    }

    @Override
    public List<Pair<Object, CellStyle>> getCells(XSSFWorkbook workbook, Customer customer) {
        CellStyle style = ExcelCellStyleUtil.getDefaultCellStyle(workbook);

        return List.of(
                Pair.create(customer.getId(), style),
                Pair.create(customer.getCustomerType().getCode(), style),
                Pair.create(getFirstName(customer), style),
                Pair.create(getLastName(customer), style),
                Pair.create(getPesel(customer), style),
                Pair.create(getGender(customer), style),
                Pair.create(customer.getEmployeeFlg(), style),
                Pair.create(getName(customer), style),
                Pair.create(getNip(customer), style),
                Pair.create(customer.getCustomerAddress().getCountry().getName(), style),
                Pair.create(customer.getCustomerAddress().getPlace(), style),
                Pair.create(getStreet(customer), style),
                Pair.create(customer.getCustomerAddress().getHouseNumber(), style),
                Pair.create(getFlatNumber(customer), style),
                Pair.create(getZipCode(customer), style),
                Pair.create(getEmail(customer), style),
                Pair.create(getCellPhone(customer), style),
                Pair.create(getKrs(customer), style),
                Pair.create(getRegon(customer), style)
        );
    }

    private String getFirstName(Customer customer) {
        return Optional.ofNullable(customer.getFirstName()).orElse("");
    }

    private String getLastName(Customer customer) {
        return Optional.ofNullable(customer.getLastName()).orElse("");
    }

    private String getPesel(Customer customer) {
        return Optional.ofNullable(customer.getPesel()).orElse("");
    }

    private String getName(Customer customer) {
        return Optional.ofNullable(customer.getName()).orElse("");
    }

    private String getNip(Customer customer) {
        return Optional.ofNullable(customer.getNip()).orElse("");
    }

    private String getStreet(Customer customer) {
        return Optional.ofNullable(customer.getCustomerAddress().getStreet()).orElse("");
    }

    private String getFlatNumber(Customer customer) {
        return Optional.ofNullable(customer.getCustomerAddress().getFlatNumber()).orElse("");
    }

    private String getZipCode(Customer customer) {
        return Optional.ofNullable(customer.getCustomerAddress().getZipCode()).orElse("");
    }

    private String getEmail(Customer customer) {
        return Optional.ofNullable(customer.getCustomerDetails()).map(CustomerDetails::getEmail).orElse("");
    }

    private String getCellPhone(Customer customer) {
        return Optional.ofNullable(customer.getCustomerDetails()).map(CustomerDetails::getCellPhone).orElse("");
    }

    private String getKrs(Customer customer) {
        return Optional.ofNullable(customer.getCustomerDetails()).map(CustomerDetails::getKrs).orElse("");
    }

    private String getRegon(Customer customer) {
        return Optional.ofNullable(customer.getCustomerDetails()).map(CustomerDetails::getRegon).orElse("");
    }

    private String getGender(Customer customer) {
        return Optional.ofNullable(customer.getCustomerGender()).map(CustomerGender::getCode).orElse("");
    }
}