package controlling.contractRegister.dic;

public enum CustomerType {

    PERSON("Osoba"),
    BUSINESS("Firma");

    private final String code;

    CustomerType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}