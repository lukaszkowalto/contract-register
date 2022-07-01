package controlling.contractRegister.dic;

public enum UserRole {

    USER("Użytkownik"),
    ADMIN("Administrator");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}