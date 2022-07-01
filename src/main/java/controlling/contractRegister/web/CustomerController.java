package controlling.contractRegister.web;

import controlling.contractRegister.pagination.PageSize;
import controlling.contractRegister.service.CustomerService;
import controlling.contractRegister.service.ExcelExporterService;
import controlling.contractRegister.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ExcelExporterService excelExporterService;

    @PostMapping("/customer")
    public String saveCustomer(@ModelAttribute("customerForm") @Valid CustomerDTO customerDTO, String callingInterface, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("ERROR: " +
                    bindingResult.getAllErrors().stream().map(ObjectError::getObjectName).collect(Collectors.joining(",")));
        }
        customerService.save(customerDTO);
        return "redirect:/" + callingInterface;
    }

    @GetMapping(value = "/customers")
    public String listCustomers(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                @RequestParam(value = "size", required = false, defaultValue = PageSize.DEFAULT) int pageSize, Model model) {
        model.addAttribute("customers", customerService.getPage(pageNumber, pageSize));
        model.addAttribute("customerForm", new CustomerDTO());
        model.addAttribute("callingInterface", "customers");
        return "select-customer";
    }

    @PostMapping(value = "/customers")
    public String filterCustomers(@RequestParam(value = "pattern", required = false) String pattern,
                                  @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "size", required = false, defaultValue = PageSize.DEFAULT) int pageSize, Model model) {
        model.addAttribute("customers", customerService.getPageFiltered(StringUtil.replaceNullWithEmptyString(pattern), pageNumber, pageSize));
        model.addAttribute("customerForm", new CustomerDTO());
        model.addAttribute("callingInterface", "customers");
        return "select-customer";
    }

    @GetMapping(value = "/delete-customer")
    public String deleteCustomer(@RequestParam("id") Integer id, Model model) {
        if (customerService.existsCustomerById(id)) {
            customerService.deleteCustomerById(id);
        }
        return "redirect:/customers";
    }

    @PostMapping(value = "/update-customer")
    public String updateCustomer(
            @ModelAttribute("customerForm") @Valid CustomerDTO customerDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("ERROR: " +
                    bindingResult.getAllErrors().stream().map(ObjectError::getObjectName).collect(Collectors.joining(",")));
        }
        customerService.save(customerDTO);
        return "redirect:/customers";
    }
}