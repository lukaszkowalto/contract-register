package controlling.contractRegister.dic;

public enum Currency {

    PLN("złoty polski"),
    THB("bat (Tajlandia)"),
    USD("dolar amerykański"),
    AUD("dolar australijski"),
    HKD("dolar hongkoński"),
    CAD("dolar kanadyjski"),
    NZD("dolar nowozelandzki"),
    SGD("dolar singapurski"),
    EUR("euro"),
    HUF("forint węgierski"),
    CHF("frank szwajcarski"),
    GBP("funt szterling"),
    UAH("hrywna ukraińska"),
    JPY("jen japoński"),
    CZK("korona czeska"),
    DKK("korona duńska"),
    ISK("korona islandzka"),
    NOK("korona norweska"),
    SEK("korona szwedzka"),
    HRK("kuna chorwacka"),
    RON("lej rumuński"),
    BGN("lew bułgarski"),
    TRY("lira turecka"),
    ILS("n. szekel"),
    CLP("peso"),
    PHP("peso filipińskie"),
    MXN("peso meksykańskie"),
    ZAR("rand RPA"),
    BRL("real brazylijski"),
    MYR("ringgit malezyjski"),
    RUB("rubel rosyjski"),
    INR("rupia"),
    IDR("rupia (Indonezja)"),
    KRW("won (Korea Płd.)"),
    CNY("yuan renminbi");

    private final String name;

    Currency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}