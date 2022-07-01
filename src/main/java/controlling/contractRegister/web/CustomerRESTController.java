package controlling.contractRegister.web;

import controlling.contractRegister.export.CustomerExportObject;
import controlling.contractRegister.model.Customer;
import controlling.contractRegister.service.CustomerService;
import controlling.contractRegister.service.ExcelExporterService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class CustomerRESTController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ExcelExporterService excelExporterService;

    @ResponseBody
    @RequestMapping(value = "/getCustomerNipValidationResult/{nip}", method = RequestMethod.GET)
    public String validateCustomerNip(@PathVariable("nip") String nip) {
        boolean result = customerService.existsCustomerByNip(nip);
        JSONObject json = new JSONObject();
        json.put("result", Boolean.toString(!result));

        if (result) {
            json.put("message", String.format(messageSource.getMessage("add.customer.modal.step1.content.customer-nip-validation-error", null, LocaleContextHolder.getLocale()), nip));
        }

        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/getCustomerPeselValidationResult/{pesel}", method = RequestMethod.GET)
    public String validateCustomerPesel(@PathVariable("pesel") String pesel) {
        boolean result = customerService.existsCustomerByPesel(pesel);
        JSONObject json = new JSONObject();
        json.put("result", Boolean.toString(!result));

        if (result) {
            json.put("message", String.format(messageSource.getMessage("add.customer.modal.step1.content.customer-pesel-validation-error", null, LocaleContextHolder.getLocale()), pesel));
        }

        return json.toString();
    }

    @GetMapping("/customers/export/excel")
    public void exportCustomersToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customers_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Customer> customers = customerService.getAllCustomers();

        excelExporterService.export(new CustomerExportObject(customers), response.getOutputStream());
    }
}