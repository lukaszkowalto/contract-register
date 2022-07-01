package controlling.contractRegister.dic;

import java.util.Arrays;
import java.util.stream.Stream;

public enum CategoryType {

    ACCOUNTING("Księgowe"),
    CONTROLLING("Kontrolingowe"),
    OTHER("Inne");

    private final String name;

    CategoryType(String name) {
        this.name = name;
    }

    public static Stream<CategoryType> findByNameLike(String name) {
        return Arrays.stream(values()).filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()));
    }

    public String getName() {
        return name;
    }
}