package controlling.contractRegister.util;

import java.text.DecimalFormat;

public class IntegerUtil {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static long getTrend(Integer current, Integer previous) {
        if (previous == 0) {
            return 100;
        }

        return Math.round(100.0 * (current - previous) / previous);
    }
}
