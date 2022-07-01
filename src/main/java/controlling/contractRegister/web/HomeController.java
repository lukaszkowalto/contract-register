package controlling.contractRegister.web;

import controlling.contractRegister.service.CategoryService;
import controlling.contractRegister.service.ContractService;
import controlling.contractRegister.service.CustomerService;
import controlling.contractRegister.service.ProjectService;
import controlling.contractRegister.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class HomeController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProjectService projectService;

    @GetMapping({"/home", "/"})
    public String showHomePage(Model model) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime back30days = LocalDateTime.now().minusDays(30);
        LocalDateTime back60days = LocalDateTime.now().minusDays(60);

        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("years", DateUtil.getAvailableYears());
        model.addAttribute("contractForm", new ContractDTO());
        model.addAttribute("categoryForm", new CategoryDTO());
        model.addAttribute("projectForm", new ProjectDTO());
        model.addAttribute("customerForm", new CustomerDTO());
        model.addAttribute("contractStats", new ContractStatsDTO(
                contractService.getNumberOfContractsByCreateDateTimeBetween(back30days, now),
                contractService.getNumberOfContractsByCreateDateTimeBetween(back60days, back30days),
                contractService.getSumOfContractsGrossAmountByCreateDateTimeBetween(back30days, now),
                contractService.getSumOfContractsGrossAmountByCreateDateTimeBetween(back60days, back30days)));
        model.addAttribute("contracts", contractService.getTop5ContractsOrderByIdDesc());
        model.addAttribute("callingInterface", "home");
        return "home";
    }
}