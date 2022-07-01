package controlling.contractRegister.web;

import controlling.contractRegister.pagination.PageSize;
import controlling.contractRegister.service.CategoryService;
import controlling.contractRegister.service.ContractService;
import controlling.contractRegister.service.CustomerService;
import controlling.contractRegister.service.ProjectService;
import controlling.contractRegister.util.DateUtil;
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
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/contract")
    public String saveContract(@ModelAttribute("contractForm") @Valid ContractDTO contractDTO, String callingInterface, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("ERROR: " +
                    bindingResult.getAllErrors().stream().map(ObjectError::getObjectName).collect(Collectors.joining(",")));
        }
        contractService.save(contractDTO);

        return "redirect:/" + callingInterface;
    }

    @GetMapping(value = "/contracts")
    public String listContracts(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                @RequestParam(value = "size", required = false, defaultValue = PageSize.DEFAULT) int pageSize, Model model) {
        model.addAttribute("years", DateUtil.getAvailableYears());
        model.addAttribute("contracts", contractService.getPage(pageNumber, pageSize));
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("contractForm", new ContractDTO());
        model.addAttribute("callingInterface", "contracts");
        return "select-contract";
    }

    @PostMapping(value = "/contracts")
    public String filterContracts(@RequestParam(value = "pattern", required = false) String pattern,
                                  @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                  @RequestParam(value = "size", required = false, defaultValue = PageSize.DEFAULT) int pageSize, Model model) {
        model.addAttribute("years", DateUtil.getAvailableYears());
        model.addAttribute("contracts", contractService.getPageFiltered(StringUtil.replaceNullWithEmptyString(pattern), pageNumber, pageSize));
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("contractForm", new ContractDTO());
        model.addAttribute("callingInterface", "contracts");
        return "select-contract";
    }

    @GetMapping(value = "/delete-contract")
    public String deleteContract(@RequestParam("id") Integer id) {
        if (contractService.existsContractById(id)) {
            contractService.deleteContractById(id);
        }
        return "redirect:/contracts";
    }

    @PostMapping(value = "/update-contract")
    public String updateContract(@ModelAttribute("contractForm") @Valid ContractDTO contractDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("ERROR: " +
                    bindingResult.getAllErrors().stream().map(ObjectError::getObjectName).collect(Collectors.joining(",")));
        }
        contractService.save(contractDTO);
        return "redirect:/contracts";
    }
}