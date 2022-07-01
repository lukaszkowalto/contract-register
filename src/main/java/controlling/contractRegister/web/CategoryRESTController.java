package controlling.contractRegister.web;

import com.google.gson.Gson;
import controlling.contractRegister.dic.CategoryType;
import controlling.contractRegister.export.CategoryExportObject;
import controlling.contractRegister.model.Category;
import controlling.contractRegister.service.CategoryService;
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
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CategoryRESTController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ExcelExporterService excelExporterService;

    @ResponseBody
    @RequestMapping(value = "/getCategorySymbolValidationResult/{year}/{symbol}", method = RequestMethod.GET)
    public String validateProjectSymbol(@PathVariable("year") double year,
                                        @PathVariable("symbol") String symbol) {
        boolean result = categoryService.existsCategoryByYearAndSymbol(year, symbol);
        JSONObject json = new JSONObject();
        json.put("result", Boolean.toString(!result));

        if (result) {
            json.put("message", String.format(messageSource.getMessage("add.category.modal.step2.content.category-symbol-validation-error", null, LocaleContextHolder.getLocale()), symbol));
        }

        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/getFDMCategoryList/{year}", method = RequestMethod.GET)
    public String fdmCategories(@PathVariable("year") double year) {
        Gson gson = new Gson();
        List<List<?>> fdmCategoriesResponse = new ArrayList<>();

        List<Category> fdmCategories = categoryService.getFDMCategories(year);
        List<Integer> fdmCategoriesId = fdmCategories.stream().map(Category::getId).collect(Collectors.toList());
        List<String> fdmCategoriesHeader = fdmCategories.stream().map(Category::getCategoryHeader).collect(Collectors.toList());
        fdmCategoriesResponse.add(fdmCategoriesId);
        fdmCategoriesResponse.add(fdmCategoriesHeader);

        return gson.toJson(fdmCategoriesResponse);
    }

    @ResponseBody
    @RequestMapping(value = {"/getParentCategoryList/{year}/{type}", "/getParentCategoryList/{year}/{type}/{id}"}, method = RequestMethod.GET)
    public String filterParentCategories(@PathVariable("year") double year,
                                         @PathVariable("type") CategoryType categoryType,
                                         @PathVariable(required = false) Integer id) {
        Gson gson = new Gson();

        int idToCompare = Optional.ofNullable(id).orElse(-1);

        List<Category> parentCategories = categoryService.getCategoriesByYearAndCategoryType(year, categoryType);
        List<Category> childCategories = categoryService.getAllChildCategories(id);
        Map<Integer, String> categoryIdToCategoryHeader = parentCategories.stream()
                .filter(c -> !c.getId().equals(idToCompare))
                .filter(c -> !childCategories.contains(c))
                .collect(Collectors.toMap(Category::getId, Category::getCategoryHeader));

        return gson.toJson(categoryIdToCategoryHeader);
    }

    @GetMapping("/categories/export/excel")
    public void exportCategoriesToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=categories_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Category> categories = categoryService.getAllCategories();

        excelExporterService.export(new CategoryExportObject(categories), response.getOutputStream());
    }
}