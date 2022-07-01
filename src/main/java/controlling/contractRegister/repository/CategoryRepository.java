package controlling.contractRegister.repository;

import controlling.contractRegister.dic.CategoryType;
import controlling.contractRegister.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByYearAndCategoryType(double year, CategoryType categoryType);

    List<Category> findByParentCategory(Category parentCategory);

    boolean existsByYearAndSymbol(double year, String symbol);

    @Query("SELECT i FROM Category i WHERE \n" +
            "\t\t\t LOWER(coalesce(i.symbol, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\t OR  LOWER(coalesce(i.name, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\t OR  LOWER(coalesce(i.description, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\t OR  LOWER(cast(i.year as string)) LIKE LOWER(CONCAT('%', :pattern,'%'))")
    Page<Category> getAllCategoriesFiltered(@Param("pattern") String pattern, Pageable pageable);

    @Query("SELECT i FROM Category i WHERE i.year=(:year) \n" +
            "\t\t AND NOT EXISTS (SELECT 1 FROM Category ii WHERE ii.parentCategory.id = i.id)")
    List<Category> getFDMCategories(@Param("year") double year);

    @Query(value = "WITH CTE AS\n" +
            "(\n" +
            "  SELECT C.id, C.parent_category_id\n" +
            "  FROM dbo.category C\n" +
            "  WHERE C.parent_category_id = :id\n" +
            "\n" +
            "  UNION ALL\n" +
            "\n" +
            "  SELECT C1.id, C1.parent_category_id\n" +
            "  FROM dbo.category C1  \n" +
            "  INNER JOIN CTE M\n" +
            "  ON M.id = C1.parent_category_id\n" +
            " )\n" +
            "SELECT * FROM dbo.category C\n" +
            "JOIN CTE E ON E.id=C.id",
            nativeQuery = true)
    List<Category> getChildCategories(@Param("id") Integer id);
}