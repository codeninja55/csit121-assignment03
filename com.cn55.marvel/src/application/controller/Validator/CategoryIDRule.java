package application.controller.Validator;

import java.util.regex.Pattern;

public class CategoryIDRule implements FormRule {

    private static final String ID_PATTERN = "\\d*";
    private static final Pattern regex = Pattern.compile(ID_PATTERN);

    public boolean validate(FormValidData validData) {
        return regex.matcher(validData.getCategoryID()).matches();
    }
}
