package controlling.contractRegister.web;

import controlling.contractRegister.pagination.PageSize;
import controlling.contractRegister.service.CategoryService;
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
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/category")
    public String saveCategory(@ModelAttribute("categoryForm") @Valid CategoryDTO categoryDTO, String callingInterface, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("ERROR: " +
                    bindingResult.getAllErrors().stream().map(ObjectError::getObjectName).collect(Collectors.joining(",")));
        }
        categoryService.save(categoryDTO);
        return "redirect:/" + callingInterface;
    }

    @GetMapping(value = "/categories")
    public String listCategories(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                 @RequestParam(value = "size", required = false, defaultValue = PageSize.DEFAULT) int pageSize, Model model) {
        model.addAttribute("years", DateUtil.getAvailableYears());
        model.addAttribute("categories", categoryService.getPage(pageNumber, pageSize));
        model.addAttribute("categoryForm", new CategoryDTO());
        model.addAttribute("callingInterface", "categories");
        return "select-category";
    }

    @PostMapping(value = "/categories")
    public String filterCategories(@RequestParam(value = "pattern", required = false) String pattern,
                                   @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                   @RequestParam(value = "size", required = false, defaultValue = PageSize.DEFAULT) int pageSize, Model model) {
        model.addAttribute("years", DateUtil.getAvailableYears());
        model.addAttribute("categories", categoryService.getPageFiltered(StringUtil.replaceNullWithEmptyString(pattern), pageNumber, pageSize));
        model.addAttribute("categoryForm", new CategoryDTO());
        model.addAttribute("callingInterface", "categories");
        return "select-category";
    }

    @GetMapping(value = "/delete-category")
    public String deleteCategory(@RequestParam("id") Integer id, Model model) {
        if (categoryService.existsCategoryById(id)) {
            categoryService.deleteCategoryById(id);
        }
        return "redirect:/categories";
    }

    @PostMapping(value = "/update-category")
    public String updateCategory(@ModelAttribute("categoryForm") @Valid CategoryDTO categoryDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("ERROR:" +
                    bindingResult.getAllErrors().stream().map(ObjectError::getObjectName).collect(Collectors.joining(",")));
        }
        categoryService.save(categoryDTO);
        return "redirect:/categories";
    }
}