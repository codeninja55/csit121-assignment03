package application.controller.Validator;

import java.util.regex.Pattern;

public class CategoryAmountRule implements FormRule {
    private final static String DOUBLE_PATTERN = "^[-+]?[0-9]+(,[0-9]{3})*(\\.[0-9]+)?([eE][-+]?[0-9]+)?";
    private static final Pattern regex = Pattern.compile(DOUBLE_PATTERN);

    public boolean validate(FormValidData validData) { return regex.matcher(validData.getCatValueStr()).matches(); }
}
