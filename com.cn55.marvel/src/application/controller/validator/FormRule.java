package application.controller.validator;

@SuppressWarnings("ALL")
public interface FormRule {
    boolean validate(FormValidData validData);
}
