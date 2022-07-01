package controlling.contractRegister.web;

import controlling.contractRegister.dic.ProjectType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@ToString
public class ProjectDTO {

    private Integer id;

    @NotNull
    @Min(2021)
    @Max(2100)
    private Double year;

    @NotNull
    @Size(min = 1, max = 32)
    private String symbol;

    private Integer parentProjectId;

    @NotNull
    private ProjectType projectType;

    @NotNull
    @Size(min = 1, max = 256)
    private String name;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateTo;

    @Size(max = 2048)
    private String description;
}