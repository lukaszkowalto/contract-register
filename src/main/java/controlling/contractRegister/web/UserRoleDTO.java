package controlling.contractRegister.web;

import controlling.contractRegister.dic.UserRole;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@ToString
public class UserRoleDTO {

    @NotNull
    private Integer id;

    @NotNull
    private UserRole role;
}