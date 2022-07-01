package controlling.contractRegister.model;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Table(name = "[contract_attachment]", schema = "dbo")
public class ContractAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "contract_id", referencedColumnName = "id")
    private Contract contract;

    @Column(name = "name", nullable = false)
    private String fileName;

    @Type(type = "uuid-char")
    @Column(name = "token", columnDefinition = "uniqueidentifier", nullable = false, unique = true)
    private UUID token = UUID.randomUUID();

    @Column(name = "directory", nullable = false)
    private String directory;

    @Column(name = "document_format", nullable = false)
    private String documentFormat;

    @Column(name = "description", length = 256, nullable = false)
    private String description;

    @CreationTimestamp
    @Column(name = "create_dttm", nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "update_dttm")
    private LocalDateTime updateDateTime;
}