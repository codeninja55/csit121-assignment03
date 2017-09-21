package application.controller.validator;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface ExistsRule {
    boolean existsValidating(FormValidData validData);
}
