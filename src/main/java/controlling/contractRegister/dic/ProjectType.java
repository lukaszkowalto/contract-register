package controlling.contractRegister.dic;

import java.util.Arrays;
import java.util.stream.Stream;

public enum ProjectType {

    INVESTMENT("Inwestycyjne"),
    COST_CENTER("Miejsce powstawania kosztów"),
    OTHER("Inne");

    private final String name;

    ProjectType(String name) {
        this.name = name;
    }

    public static Stream<ProjectType> findByNameLike(String name) {
        return Arrays.stream(values()).filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()));
    }

    public String getName() {
        return name;
    }
}