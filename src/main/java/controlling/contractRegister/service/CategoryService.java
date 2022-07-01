package controlling.contractRegister.service;

import controlling.contractRegister.dic.CategoryType;
import controlling.contractRegister.model.Category;
import controlling.contractRegister.pagination.Paged;
import controlling.contractRegister.web.CategoryDTO;

import java.util.List;

public interface CategoryService {

    boolean existsCategoryById(Integer categoryId);

    Category getCategoryById(Integer categoryId);

    void save(CategoryDTO categoryDTO);

    boolean existsCategoryByYearAndSymbol(double year, String symbol);

    List<Category> getAllCategories();

    List<Category> getAllChildCategories(Integer categoryId);

    List<Category> getCategoriesByYearAndCategoryType(double year, CategoryType categoryType);

    List<Category> getFDMCategories(double year);

    void deleteCategoryById(Integer categoryId);

    Paged<Category> getPage(int pageNumber, int size);

    Paged<Category> getPageFiltered(String pattern, int pageNumber, int size);
}