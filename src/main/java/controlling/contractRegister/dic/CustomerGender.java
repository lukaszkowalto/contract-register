package controlling.contractRegister.dic;

public enum CustomerGender {

    MALE("Mężczyzna"),
    FEMALE("Kobieta"),
    UNDEFINED("Nie podano");

    private final String code;

    CustomerGender(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}