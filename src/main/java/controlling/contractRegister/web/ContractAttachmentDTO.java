package controlling.contractRegister.web;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@ToString
public class ContractAttachmentDTO {

    private Integer id;

    private MultipartFile file;

    @NotNull
    @Size(min = 1, max = 256)
    private String description;
}