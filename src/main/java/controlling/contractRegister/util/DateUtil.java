package controlling.contractRegister.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    public static Date toDate(String date) {
        return Date.from(LocalDate.parse(date, DATE_TIME_FORMATTER).atStartOfDay(ZONE_ID).toInstant());
    }

    public static List<Integer> getAvailableYears() {
        List<Integer> years = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        for (int i = 0; i < 5; i++) {
            years.add(currentYear + i);
        }
        return years;
    }
}