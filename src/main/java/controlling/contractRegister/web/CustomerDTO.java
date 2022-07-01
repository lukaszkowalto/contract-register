package controlling.contractRegister.web;

import controlling.contractRegister.dic.CustomerAddressCountry;
import controlling.contractRegister.dic.CustomerGender;
import controlling.contractRegister.dic.CustomerType;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@ToString
public class CustomerDTO {

    private Boolean employeeFlg = false;

    private Integer id;

    @NotNull
    private CustomerType customerType;

    private CustomerGender customerGender;

    @Size(min = 0, max = 32)
    private String firstName;

    @Size(min = 0, max = 32)
    private String lastName;

    @Size(min = 0, max = 64)
    private String name;

    @Size(min = 0, max = 16)
    private String pesel;

    @Size(min = 0, max = 32)
    private String nip;

    @NotNull
    private CustomerAddressCountry country;

    @NotNull
    @Size(min = 1, max = 256)
    private String place;

    @Size(min = 0, max = 256)
    private String street;

    @NotNull
    @Size(min = 1, max = 16)
    private String houseNumber;

    @Size(min = 0, max = 16)
    private String flatNumber;

    @Size(min = 0, max = 16)
    private String zipCode;

    @Size(min = 0, max = 64)
    private String email;

    @Size(min = 0, max = 32)
    private String cellPhone;

    @Size(min = 0, max = 16)
    private String regon;

    @Size(min = 0, max = 16)
    private String krs;
}