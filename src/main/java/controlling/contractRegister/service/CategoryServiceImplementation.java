package controlling.contractRegister.service;

import controlling.contractRegister.dic.CategoryType;
import controlling.contractRegister.model.Category;
import controlling.contractRegister.pagination.Paged;
import controlling.contractRegister.pagination.Paging;
import controlling.contractRegister.repository.CategoryRepository;
import controlling.contractRegister.util.StringUtil;
import controlling.contractRegister.web.CategoryDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getAllChildCategories(Integer categoryId) {
        return categoryRepository.getChildCategories(categoryId);
    }

    @Override
    public List<Category> getCategoriesByYearAndCategoryType(double year, CategoryType categoryType) {
        return categoryRepository.findByYearAndCategoryType(year, categoryType);
    }

    @Override
    public List<Category> getFDMCategories(double year) {
        return categoryRepository.getFDMCategories(year);
    }

    @Override
    public void deleteCategoryById(Integer categoryId) {
        onDeleteSetNull(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Paged<Category> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("id").ascending());
        Page<Category> categoryPage = categoryRepository.findAll(request);
        return new Paged<>(categoryPage, Paging.of(categoryPage.getTotalPages(), pageNumber, size));
    }

    @Override
    public Paged<Category> getPageFiltered(String pattern, int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("id").ascending());
        Page<Category> categoryPage = categoryRepository.getAllCategoriesFiltered(pattern, request);
        return new Paged<>(categoryPage, Paging.of(categoryPage.getTotalPages(), pageNumber, size));
    }

    @Override
    public boolean existsCategoryById(Integer categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    @Override
    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException(" Category not found for id :: " + categoryId));
    }

    @Override
    public void save(CategoryDTO categoryDTO) {
        Category categoryDB = new Category();
        BeanUtils.copyProperties(categoryDTO, categoryDB);
        if (categoryDTO.getParentCategoryId() != null && existsCategoryById(categoryDTO.getParentCategoryId())) {
            categoryDB.setParentCategory(getCategoryById(categoryDTO.getParentCategoryId()));
        }
        categoryDB.setDescription(StringUtil.replaceEmptyStringWithNull(categoryDTO.getDescription()));

        categoryRepository.save(categoryDB);
    }

    @Override
    public boolean existsCategoryByYearAndSymbol(double year, String symbol) {
        return categoryRepository.existsByYearAndSymbol(year, symbol);
    }

    private void onDeleteSetNull(Integer parentCategoryId) {
        for (Category childCategory : categoryRepository.findByParentCategory(categoryRepository.getById(parentCategoryId))) {
            childCategory.setParentCategory(null);
            categoryRepository.save(childCategory);
        }
    }
}