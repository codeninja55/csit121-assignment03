package styles;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class FormatterFactory {
    private static NumberFormat formatter;
    public static NumberFormat doubleFormat() {
        formatter = DecimalFormat.getInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter;
    }
    public static NumberFormat currencyFormat() {
        formatter = DecimalFormat.getCurrencyInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter;
    }
    public static NumberFormat pointsFormat() {
        return doubleFormat();
    }
    public static NumberFormat integerFormat() {
        formatter = DecimalFormat.getInstance();
        formatter.setMaximumFractionDigits(0);
        formatter.setMinimumFractionDigits(0);
        return formatter;
    }
}
