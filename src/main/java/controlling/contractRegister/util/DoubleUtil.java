package controlling.contractRegister.util;

public class DoubleUtil {

    public static long getTrend(double current, double previous) {
        if (previous == 0.0) {
            return 100;
        }
        return Math.round(100.0 * (current - previous) / previous);
    }
}