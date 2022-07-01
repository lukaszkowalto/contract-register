package controlling.contractRegister.pagination;

public enum PageSize {
    SMALL("5"),
    MEDIUM("10"),
    BIG("20"),
    HUGE("40");

    public static final String DEFAULT = "5";

    private final String value;

    PageSize(String value) {
        this.value = value;
    }

    public String getValue() {
        return String.valueOf(value);
    }
}