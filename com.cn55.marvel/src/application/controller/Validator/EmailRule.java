package application.controller.Validator;

import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class EmailRule implements FormRule {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    public boolean validate(FormValidData validData) {
        return pattern.matcher(validData.getEmail()).matches();
    }
}
