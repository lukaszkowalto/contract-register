package controlling.contractRegister.model;

import controlling.contractRegister.dic.CategoryType;
import controlling.contractRegister.util.StringUtil;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Table(name = "[category]", schema = "dbo")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 4, scale = 0)
    private double year;

    @Column(nullable = false, length = 32)
    private String symbol;

    @ManyToOne(optional = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", length = 32, nullable = false)
    private CategoryType categoryType;

    @Column(length = 256, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(name = "create_dttm", nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "update_dttm")
    private LocalDateTime updateDateTime;

    public String getCategoryHeader() {
        return StringUtil.replaceNullWithEmptyString(symbol) + " " + StringUtil.replaceNullWithEmptyString(name);
    }
}