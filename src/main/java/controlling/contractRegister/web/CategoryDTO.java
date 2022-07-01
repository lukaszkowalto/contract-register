package controlling.contractRegister.web;

import controlling.contractRegister.dic.CategoryType;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@ToString
public class CategoryDTO {

    private Integer id;

    @NotNull
    @Min(2021)
    @Max(2100)
    private Double year;

    @NotNull
    @Size(min = 1, max = 32)
    private String symbol;

    private Integer parentCategoryId;

    @NotNull
    private CategoryType categoryType;

    @NotNull
    @Size(min = 1, max = 256)
    private String name;

    @Size(max = 2048)
    private String description;
}