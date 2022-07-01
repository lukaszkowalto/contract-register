package controlling.contractRegister.dic;

public enum ContractType {

    MANDATE_CONTRACT("Umowa zlecenie"), WORK_CONTRACT("Umowa o dzieło"), B2B_CONTRACT("Kontract B2B"), OTHER("Inny");

    private final String name;

    ContractType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}