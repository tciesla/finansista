package pl.tciesla.assets.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class StringUtils {

    private static NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
    private static NumberFormat percentInstance = NumberFormat.getPercentInstance();

    static {
        currencyInstance.setMinimumFractionDigits(2);
        currencyInstance.setMaximumFractionDigits(2);
        percentInstance.setMinimumFractionDigits(2);
        percentInstance.setMaximumFractionDigits(2);
    }

    public static String toCurrency(BigDecimal value) {
        return currencyInstance.format(value);
    }

    public static String toPercent(BigDecimal value) {
        return percentInstance.format(value);
    }
}
