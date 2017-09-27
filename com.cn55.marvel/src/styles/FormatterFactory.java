package styles;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class FormatterFactory {
    public static NumberFormat currencyFormat() {
        NumberFormat formatter = DecimalFormat.getCurrencyInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter;
    }
    public static NumberFormat pointsFormat() {
        NumberFormat formatter = DecimalFormat.getInstance();
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        return formatter;
    }
}
