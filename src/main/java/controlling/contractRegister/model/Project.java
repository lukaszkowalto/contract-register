package controlling.contractRegister.model;

import controlling.contractRegister.dic.ProjectType;
import controlling.contractRegister.util.StringUtil;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Table(name = "[project]", schema = "dbo")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 4, scale = 0)
    private double year;

    @Column(nullable = false, length = 32)
    private String symbol;

    @ManyToOne(optional = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "parent_project_id")
    private Project parentProject;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_type", length = 32, nullable = false)
    private ProjectType projectType;

    @Column(length = 256, nullable = false)
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "date_from", nullable = false)
    private Date dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "date_to", nullable = false)
    private Date dateTo;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(name = "create_dttm", nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "update_dttm")
    private LocalDateTime updateDateTime;

    public String getProjectHeader() {
        return StringUtil.replaceNullWithEmptyString(symbol) + " " + StringUtil.replaceNullWithEmptyString(name);
    }
}